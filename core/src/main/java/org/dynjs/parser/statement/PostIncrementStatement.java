package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

public class PostIncrementStatement extends AbstractUnaryOperationStatement {

    public PostIncrementStatement(Statement expression) {
        super(expression);
    }

    protected String operation() {
        return "add";
    }

    @Override
    protected CodeBlock before() {
        return store();
    }
}
