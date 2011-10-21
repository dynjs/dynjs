package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class NotEqualsOperationStatement implements Statement {

    private final Statement l;
    private final Statement r;

    public NotEqualsOperationStatement(Statement l, Statement r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .append(l.getCodeBlock())
                .append(r.getCodeBlock())
                .invokedynamic("neq", sig(Boolean.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);

    }
}
