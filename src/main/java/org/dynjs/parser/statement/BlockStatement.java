package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

import java.util.List;

public class BlockStatement implements Statement {
    private final CodeBlock codeBlock;

    public BlockStatement(final List<Statement> blockContent) {
        this.codeBlock = new CodeBlock() {{
            for (Statement statement : blockContent) {
                append(statement.getCodeBlock());
            }
        }};
    }

    @Override
    public CodeBlock getCodeBlock() {
        return this.codeBlock;
    }
}
