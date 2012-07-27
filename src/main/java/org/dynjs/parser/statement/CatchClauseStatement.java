package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class CatchClauseStatement extends BaseStatement implements Statement {
    private final DynThreadContext context;
    private final String id;
    private final Statement block;

    public CatchClauseStatement(Tree tree, DynThreadContext context, String id, Statement block) {
        super(tree);
        this.context = context;
        this.id = id;
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            final Integer slot = context.store(block.getCodeBlock());

            aload(DynJSCompiler.Arities.CONTEXT);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "getRuntime", sig(DynJS.class));

            ldc("ExceptionHandlerBlock");
            aload(DynJSCompiler.Arities.CONTEXT);
            dup();
            bipush(slot);
            invokevirtual(DynJSCompiler.Types.CONTEXT, "retrieve", sig(CodeBlock.class, int.class));

            ldc(id);
            invokevirtual(DynJSCompiler.Types.RUNTIME, "compileBasicBlock", sig(Object.class, String.class, DynThreadContext.class, CodeBlock.class, String.class));
        }};
    }
}
