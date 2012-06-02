package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ThisStatement extends BaseStatement implements Statement {
    public ThisStatement(Tree tree) {
        super(tree);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock(){{
            aload(0);
            aload(1);
            invokedynamic("this", sig(Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
        }};
    }
}
