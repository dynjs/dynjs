package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ResolveByFieldStatement implements Statement {

    private final Statement lhs;
    private final String field;

    public ResolveByFieldStatement(Statement lhs, String field) {
        this.lhs = lhs;
        this.field = field;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .append(lhs.getCodeBlock())
                .ldc(field)
                .invokeinterface(DynJSCompiler.Types.Scope, "resolve", sig(Object.class, String.class));
    }
}
