package org.dynjs.parser.statement;

import me.qmx.internal.org.objectweb.asm.tree.LabelNode;
import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class IfStatement implements Statement {

    private final DynThreadContext context;
    private final Statement vbool;
    private final Statement vthen;
    private final Statement velse;

    public IfStatement(DynThreadContext context, Statement vbool, Statement vthen, Statement velse) {
        this.context = context;
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }

    @Override
    public CodeBlock getCodeBlock() {
        LabelNode elseBlock = new LabelNode();
        LabelNode outBlock = new LabelNode();
        CodeBlock codeBlock = CodeBlock.newCodeBlock()
                .append(vbool.getCodeBlock())
                .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
                .iffalse(elseBlock)
                .append(vthen.getCodeBlock())
                .go_to(outBlock)
                .label(elseBlock)
                .append(velse.getCodeBlock())
                .label(outBlock);
        return codeBlock;
    }
}
