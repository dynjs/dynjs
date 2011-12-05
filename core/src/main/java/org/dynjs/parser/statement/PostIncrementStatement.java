package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class PostIncrementStatement implements Statement {

    private final Statement expression;

    public PostIncrementStatement(Statement expression) {
        this.expression = expression;
    }

    protected String operation() {
        return "add";
    }

    public CodeBlock getCodeBlock() {
        final ResolveIdentifierStatement resolvable = (ResolveIdentifierStatement) expression;
        return CodeBlock.newCodeBlock()
                .append(expression.getCodeBlock())
                .dup()
                .astore(4)
                .append(new NumberLiteralStatement("1", 10).getCodeBlock())
                .invokedynamic(this.operation(), sig(Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .aload(DynJSCompiler.Arities.THIS)
                .swap()
                .ldc(resolvable.getName())
                .swap()
                .invokeinterface(DynJSCompiler.Types.Scope, "define", sig(void.class, String.class, Object.class))
                .aload(4);
    }
}
