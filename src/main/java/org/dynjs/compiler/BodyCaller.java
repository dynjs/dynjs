package org.dynjs.compiler;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.codegen.AbstractCodeGeneratingVisitor.Arities;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.Completion.Type;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class BodyCaller extends CodeBlock {

    public BodyCaller(final String className, final int numBodies) {
        LabelNode abrupt = new LabelNode();
        LabelNode end = new LabelNode();

        invokestatic(p(Completion.class), "createNormal", sig(Completion.class));
        // completion
        astore(Arities.COMPLETION);
        // <empty>

        for (int i = 0; i < numBodies; ++i) {
            LabelNode nonAbrupt = new LabelNode();
            LabelNode bringForwardValue = new LabelNode();
            LabelNode nextStatement = new LabelNode();
            
            // <empty>
            aload( Arities.THIS );
            // this
            aload( Arities.EXECUTION_CONTEXT );
            // this context
            invokevirtual(className.replace(".", "/"), "call_" + i, sig(Completion.class, ExecutionContext.class));
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
        dup();
        // completion completion
        getfield(p(Completion.class), "type", ci(Completion.Type.class));
        // completion type
        invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
        // completion type
        ldc(Completion.Type.RETURN.ordinal());
        // completion type RETURN

        LabelNode returnValue = new LabelNode();
        if_icmpeq(returnValue);
        // completion
        pop();
        getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
        // UNDEF
        areturn();

        label(returnValue);
        // completion
        getfield(p(Completion.class), "value", ci(Object.class));
        areturn();
        
    }

}
