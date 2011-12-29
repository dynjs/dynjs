package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class NamedValueStatement implements Statement {

    private final Statement propertyName;
    private final Statement expr;

    public NamedValueStatement(Statement propertyName, Statement expr) {
        this.propertyName = propertyName;
        this.expr = expr;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .append(propertyName.getCodeBlock())
                .append(expr.getCodeBlock())
                .invokeinterface(DynJSCompiler.Types.Scope, "define", sig(void.class, String.class, Object.class));
    }
}
