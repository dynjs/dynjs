package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.objectweb.asm.tree.LabelNode;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class TryCatchFinallyStatement extends BaseStatement implements Statement {
    private final Statement tryBlock;
    private final Statement catchBlock;
    private final Statement finallyBlock;
    private final LabelNode blockStart = new LabelNode();
    private final LabelNode catchStart = new LabelNode();
    private final LabelNode finallyStart = new LabelNode();

    public TryCatchFinallyStatement(Tree tree, Statement tryBlock, Statement catchBlock, Statement finallyBlock) {
        super(tree);
        this.tryBlock = tryBlock;
        this.catchBlock = catchBlock;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            aload(DynJSCompiler.Arities.SELF);
            aload(DynJSCompiler.Arities.CONTEXT);
            aconst_null();
            append(catchBlock.getCodeBlock());
            if (hasFinallyBlock()) {
                append(finallyBlock.getCodeBlock());
            } else {
                aconst_null();
            }
            invokedynamic("trycatchfinally", sig(void.class, Object.class, DynThreadContext.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP_2, RT.BOOTSTRAP_ARGS);
        }};
    }

    private boolean hasFinallyBlock() {
        return finallyBlock != null;
    }
}
