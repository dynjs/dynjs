package org.dynjs.compiler.bytecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;

public class Chunker {

    public static final int CHUNK_LIMIT = 200;
    public static final int STATEMENT_THRESHOLD = 1_000;
    public static final int CHUNKS_PER_CLASS = 50;

    public static List<BlockStatement> chunk(BlockStatement block) {
        return chunk(block, CHUNK_LIMIT);
    }

    public static List<BlockStatement> chunk(BlockStatement block, int chunkSize) {
        int size = 0;

        List<Statement> statements = block.getBlockContent();

        if (statements.size() <= chunkSize) {
            return Collections.singletonList(block);
        }

        List<BlockStatement> chunks = new ArrayList<>();

        int chunkStart = 0;
        int chunkEnd = 0;

        for (Statement each : statements) {
            ++chunkEnd;
            int statementSize = each.getSizeMetric();
            if (statementSize > STATEMENT_THRESHOLD) {
                // huge statement, will be replaced by smaller interpreted statement
                statementSize = 5;
            }
            if (size + statementSize > chunkSize) {
                List<Statement> chunk = statements.subList(chunkStart, chunkEnd);
                chunks.add(new BlockStatement(chunk));
                chunkStart = chunkEnd;
                size = 0;
            } else {
                size += statementSize;
            }
        }

        if (chunkStart < statements.size()) {
            chunks.add(new BlockStatement(statements.subList(chunkStart, statements.size())));
        }

        return chunks;
    }

}
