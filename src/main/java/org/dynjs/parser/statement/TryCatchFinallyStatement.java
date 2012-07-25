package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.objectweb.asm.tree.LabelNode;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class TryCatchFinallyStatement extends BaseStatement implements Statement {
    private final DynThreadContext context;
    private final Statement tryBlock;
    private final Statement catchBlock;
    private final Statement finallyBlock;
    private final LabelNode blockStart = new LabelNode();
    private final LabelNode catchStart = new LabelNode();
    private final LabelNode finallyStart = new LabelNode();

    public TryCatchFinallyStatement(Tree tree, DynThreadContext context, Statement tryBlock, Statement catchBlock, Statement finallyBlock) {
        super(tree);
        this.context = context;
        this.tryBlock = tryBlock;
        this.catchBlock = catchBlock;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            aload(DynJSCompiler.Arities.CONTEXT);
            append(compileTryBlock(tryBlock.getCodeBlock()));
            if (hasCatchBlock()) {
                append(catchBlock.getCodeBlock());
            } else {
                aconst_null();
            }
            if (hasFinallyBlock()) {
                append(finallyBlock.getCodeBlock());
            } else {
                aconst_null();
            }
            invokedynamic("trycatchfinally", sig(void.class, DynThreadContext.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP_2, RT.BOOTSTRAP_ARGS);
        }};
    }

    private boolean hasCatchBlock() {
        return catchBlock != null;
    }

    private boolean hasFinallyBlock() {
        return finallyBlock != null;
    }

    public CodeBlock compileTryBlock(final CodeBlock block) {
        return new CodeBlock() {{
            final Integer slot = context.store(block);

            aload(DynJSCompiler.Arities.CONTEXT);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "getRuntime", sig(DynJS.class));

            aload(DynJSCompiler.Arities.CONTEXT);
            dup();
            bipush(slot);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "retrieve", sig(CodeBlock.class, int.class));

            invokevirtual(DynJSCompiler.Types.RUNTIME, "compileTryBlock", sig(Object.class, DynThreadContext.class, CodeBlock.class));
        }};
    }
}
