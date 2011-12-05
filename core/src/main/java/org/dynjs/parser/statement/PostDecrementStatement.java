package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

public class PostDecrementStatement extends AbstractUnaryOperationStatement {

    public PostDecrementStatement(Statement expression) {
        super(expression);
    }

    protected String operation() {
        return "sub";
    }

    @Override
    protected CodeBlock before() {
        return store();
    }
}
