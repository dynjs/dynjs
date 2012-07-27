package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class FinallyClauseStatement extends BaseStatement implements Statement {
    private final DynThreadContext context;
    private final Statement block;

    public FinallyClauseStatement(Tree tree, DynThreadContext context, Statement block) {
        super(tree);
        this.context = context;
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return compileFinallyBlock(block.getCodeBlock());
    }

    public CodeBlock compileFinallyBlock(final CodeBlock block) {
        return new CodeBlock() {{
            final Integer slot = context.store(block);

            aload(DynJSCompiler.Arities.CONTEXT);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "getRuntime", sig(DynJS.class));

            ldc("FinallyBlock");
            aload(DynJSCompiler.Arities.CONTEXT);
            dup();
            bipush(slot);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "retrieve", sig(CodeBlock.class, int.class));

            invokevirtual(DynJSCompiler.Types.RUNTIME, "compileBasicBlock", sig(Object.class, String.class, DynThreadContext.class, CodeBlock.class));
        }};
    }

}
