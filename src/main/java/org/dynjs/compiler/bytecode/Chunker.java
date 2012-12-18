package org.dynjs.compiler.bytecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;

public class Chunker {
    
    public static final int CHUNK_SIZE = 100;
    
    public static List<BlockStatement> chunk(BlockStatement block) {
        return chunk( block, CHUNK_SIZE );
    }
    
    public static List<BlockStatement> chunk(BlockStatement block, int chunkSize) {
        List<Statement> statements = block.getBlockContent();
        
        if ( statements.size() <= chunkSize ) {
            return Collections.singletonList( block );
        }
        
        List<BlockStatement> chunks = new ArrayList<>();
        
        int chunkStart = 0;
        
        while ( chunkStart < statements.size() ) {
            int chunkEnd = chunkStart + chunkSize;
            
            if ( chunkEnd > statements.size() ) {
                chunkEnd = statements.size();
            }
            
            List<Statement> chunk = statements.subList(chunkStart, chunkEnd);
            
            chunks.add( new BlockStatement(chunk));
            
            chunkStart = chunkEnd;
        }
        
        return chunks;
    }

}
