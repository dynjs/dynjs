package org.dynjs.codegen;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.tree.LabelNode;

public class InvokeDynamicBytecodeGeneratingVisitor extends BasicBytecodeGeneratingVisitor {
    
    public InvokeDynamicBytecodeGeneratingVisitor(BlockManager blockManager) {
        super(blockManager);
    }

    @Override
    public CodeBlock jsGetValue(final Class<?> throwIfNot) {
        return new CodeBlock() {
            {
                // IN: reference
                aload(Arities.EXECUTION_CONTEXT);
                // reference context
                invokedynamic("GetValue", sig(Object.class, Object.class, ExecutionContext.class), DynJSBootstrapper.BOOTSTRAP, DynJSBootstrapper.BOOTSTRAP_ARGS);
                // value
                if (throwIfNot != null) {
                    LabelNode end = new LabelNode();
                    dup();
                    // value value
                    instance_of(p(throwIfNot));
                    // value bool
                    iftrue(end);
                    // value
                    pop();
                    append(jsThrowTypeError("expected " + throwIfNot.getName()));
                    label(end);
                    nop();
                }
            }
        };
    }

}
