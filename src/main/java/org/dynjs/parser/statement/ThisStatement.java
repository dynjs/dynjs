package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ThisStatement extends BaseStatement implements Statement {
    public ThisStatement(Tree tree) {
        super(tree);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock(){{
            aload(DynJSCompiler.Arities.CONTEXT);
            aload(DynJSCompiler.Arities.THIS);
            aload(DynJSCompiler.Arities.SELF);
            invokedynamic("this", sig(Object.class, DynThreadContext.class, Object.class, Object.class), RT.BOOTSTRAP_2, RT.BOOTSTRAP_ARGS);
        }};
    }
}
