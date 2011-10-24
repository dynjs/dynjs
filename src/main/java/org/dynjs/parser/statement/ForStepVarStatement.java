package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ForStepVarStatement implements Statement {

    private final Statement varDef;
    private final Statement expr1;
    private final Statement expr2;
    private final BlockStatement statement;

    public ForStepVarStatement(Statement varDef, Statement expr1, Statement expr2, Statement statement) {
        this.varDef = varDef;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.statement = (BlockStatement) statement;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .append(varDef.getCodeBlock())
                .label(statement.getBeginLabel())
                .append(expr1.getCodeBlock())
                .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
                .iffalse(statement.getEndLabel())
                .append(statement.getCodeBlock())
                .append(expr2.getCodeBlock())
                .go_to(statement.getBeginLabel())
                .label(statement.getEndLabel());
    }
}
