package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.api.Function;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.objectweb.asm.tree.LabelNode;

import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class TryCatchFinallyStatement extends BaseStatement implements Statement {
    private Statement tryBlock;
    private final Statement catchBlock;
    private final Statement finallyBlock;

    public TryCatchFinallyStatement(Tree tree, Statement tryBlock, Statement catchBlock, Statement finallyBlock) {
        super(tree );
        this.tryBlock = tryBlock;
        this.catchBlock = catchBlock;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            aload(DynJSCompiler.Arities.CONTEXT);
            aload(DynJSCompiler.Arities.THIS);
            aload(DynJSCompiler.Arities.SELF);
            invokedynamic("getScope", sig(Object.class, DynThreadContext.class, Object.class, Object.class), RT.BOOTSTRAP_2, RT.BOOTSTRAP_ARGS);
            aload(DynJSCompiler.Arities.CONTEXT);
            //append( compileBasicBlockIfNecessary( "Try" ));
            if (hasCatchBlock()) {
                append(catchBlock.getCodeBlock());
            } else {
                getstatic(p(DynThreadContext.class), "NOOP", ci(Function.class));
            }
            if (hasFinallyBlock()) {
                append(finallyBlock.getCodeBlock());
            } else {
                getstatic(p(DynThreadContext.class), "NOOP", ci(Function.class));
            }
            invokedynamic("trycatchfinally", sig(void.class, Object.class, DynThreadContext.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP_2, RT.BOOTSTRAP_ARGS);
        }};
    }

    private boolean hasCatchBlock() {
        return catchBlock != null;
    }

    private boolean hasFinallyBlock() {
        return finallyBlock != null;
    }
}
