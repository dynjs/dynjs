package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class WhileStatement implements Statement {

    private final Statement vbool;
    private final BlockStatement vloop;

    public WhileStatement(Statement vbool, Statement vloop) {
        this.vbool = vbool;
        this.vloop = (BlockStatement) vloop;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock codeBlock = CodeBlock.newCodeBlock()
                .label(vloop.getBeginLabel())
                .append(vbool.getCodeBlock())
                .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
                .iffalse(vloop.getEndLabel())
                .append(vloop.getCodeBlock())
                .go_to(vloop.getBeginLabel())
                .label(vloop.getEndLabel());
        return codeBlock;
    }
}
