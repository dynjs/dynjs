package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;

public class PrintStatement implements Statement {

    private final Statement expr;

    public PrintStatement(Statement expr) {
        this.expr = expr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock(expr.getCodeBlock())
                .aprintln();
    }
}
