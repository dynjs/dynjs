package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DeclareVarStatement implements Statement {

    private final Statement expr;
    private final String id;

    public DeclareVarStatement(Statement expr, String id) {
        this.expr = expr;
        this.id = id;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock(expr.getCodeBlock())
                .astore(3)
                .aload(2)
                .ldc(id)
                .aload(3)
                .invokedynamic("dynjs:scope:define", sig(void.class, Scope.class, String.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
    }
}
