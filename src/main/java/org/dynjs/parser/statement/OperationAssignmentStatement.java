package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class OperationAssignmentStatement implements Statement {

    private final String operation;
    private final Statement l;
    private final Statement r;

    public OperationAssignmentStatement(String operation, Statement l, Statement r) {
        this.operation = operation;
        this.l = l;
        this.r = r;
    }

    @Override
    public CodeBlock getCodeBlock() {
        final ResolveIdentifierStatement resolvable = (ResolveIdentifierStatement) l;
        return CodeBlock.newCodeBlock()
                .append(l.getCodeBlock())
                .append(r.getCodeBlock())
                .invokedynamic(operation, sig(Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .aload(2)
                .swap()
                .ldc(resolvable.getName())
                .swap()
                .invokedynamic("dynjs:scope:define", sig(void.class, Scope.class, String.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);

    }
}
