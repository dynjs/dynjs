package org.dynjs.compiler.bytecode.partial;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.Completion.Type;
import org.dynjs.runtime.ExecutionContext;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;

public class MultipleClassCaller extends CodeBlock {

    public MultipleClassCaller(final String className, final int numChunks) {
        LabelNode abrupt = new LabelNode();
        LabelNode end = new LabelNode();

        invokestatic(p(Completion.class), "createNormal", sig(Completion.class));
        // completion
        astore(Arities.COMPLETION);
        // <empty>

        for (int i = 0; i < numChunks; ++i) {
            LabelNode nonAbrupt = new LabelNode();
            LabelNode bringForwardValue = new LabelNode();
            LabelNode nextStatement = new LabelNode();

            // <empty>
            aload(Arities.THIS);
            // this
            getfield(className.replace('.', '/'), "chunk" + i, ci(BasicBlock.class));
            // block
            aload(Arities.EXECUTION_CONTEXT);
            // block context
            invokeinterface(p(BasicBlock.class), "call", sig(Completion.class, ExecutionContext.class));
            // completion
            dup();
            // completion completion
            getfield(p(Completion.class), "type", ci(Completion.Type.class));
            // completion type
            invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
            // completion type-num

            lookupswitch(nonAbrupt,
                    new int[] { Type.NORMAL.ordinal(), Type.BREAK.ordinal(), Type.CONTINUE.ordinal(), Type.RETURN.ordinal() },
                    new LabelNode[] { nonAbrupt, abrupt, abrupt, abrupt });

            // ----------------------------------------
            // Non-abrupt

            label(nonAbrupt);
            // completion(cur);
            dup();
            // completion(cur) completion(cur)
            getfield(p(Completion.class), "value", ci(Object.class));
            // completion(cur) value
            ifnull(bringForwardValue);
            // completion(cur)
            astore(Arities.COMPLETION);
            // <empty>
            go_to(nextStatement);

            // ----------------------------------------

            label(bringForwardValue);
            // completion(cur)
            dup();
            // completion(cur) completion(cur)
            aload(Arities.COMPLETION);
            // completion(cur) completion(cur) completion(prev)
            getfield(p(Completion.class), "value", ci(Object.class));
            // completion(cur) completion(cur) val(prev)
            putfield(p(Completion.class), "value", ci(Object.class));
            // completion(cur)
            astore(Arities.COMPLETION);
            // <empty>
            label(nextStatement);
        }

        go_to(end);

        // ----------------------------------------
        // ABRUPT

        label(abrupt);
        // completion(cur)
        astore(Arities.COMPLETION);
        // <empty>

        // ----------------------------------------
        // END
        label(end);
        // <empty>
        aload(Arities.COMPLETION);
        // completion
        areturn();

    }

}
