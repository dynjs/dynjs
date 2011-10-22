package org.dynjs.parser.statement;

import me.qmx.internal.org.objectweb.asm.tree.LabelNode;
import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DoWhileStatement implements Statement {

private final Statement vbool;
private final Statement vloop;

public DoWhileStatement(Statement vbool, Statement vloop) {
    this.vbool = vbool;
    this.vloop = vloop;
}

@Override
public CodeBlock getCodeBlock() {
    LabelNode beginBlock = new LabelNode();
    LabelNode outBlock = new LabelNode();
    CodeBlock codeBlock = CodeBlock.newCodeBlock()
            .label(beginBlock)
            .append(vloop.getCodeBlock())
            .append(vbool.getCodeBlock())
            .invokedynamic("dynjs:convert:to_boolean", sig(Boolean.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
            .invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class))
            .iffalse(outBlock)
            .go_to(beginBlock)
            .label(outBlock);
    return codeBlock;
}
}
