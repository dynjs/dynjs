package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class RelationalOperationStatement implements Statement {

    private final String operator;
    private final Statement l;
    private final Statement r;

    public RelationalOperationStatement(String operator, Statement l, Statement r) {
        this.operator = operator;
        this.l = l;
        this.r = r;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .append(l.getCodeBlock())
                .append(r.getCodeBlock())
                .invokedynamic(operator, sig(Boolean.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
    }
}
