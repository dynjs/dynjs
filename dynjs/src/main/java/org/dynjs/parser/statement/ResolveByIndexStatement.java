package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ResolveByIndexStatement implements Statement {

    private final Statement lhs;
    private final Statement index;

    public ResolveByIndexStatement(Statement lhs, String index) {
        this(lhs, new StringLiteralStatement(index));

    }
    public ResolveByIndexStatement(Statement lhs, Statement index) {
        this.lhs = lhs;
        this.index = index;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
        .append(lhs.getCodeBlock())
        .append(index.getCodeBlock())
        .invokeinterface(DynJSCompiler.Types.Scope, "resolve", sig(Object.class, String.class));
    }
}
