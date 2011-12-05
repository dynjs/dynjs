package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;

public class PostIncrementStatement extends AbstractUnaryOperationStatement {

    public PostIncrementStatement(Statement expression) {
        super(expression);
    }

    protected String operation() {
        return "add";
    }

    @Override
    protected CodeBlock after() {
        return DynJSCompiler.Helper.EMPTY_CODEBLOCK;
    }

    @Override
    protected CodeBlock before() {
        return store();
    }
}
