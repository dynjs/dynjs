package org.dynjs.compiler.bytecode.toplevel;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.codegen.CodeGeneratingVisitor.Arities;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class FunctionCaller extends CodeBlock {
    
    public FunctionCaller() {
        LabelNode returnValue = new LabelNode();
        aload( Arities.THIS );
        // this
        aload( Arities.EXECUTION_CONTEXT );
        // this context
        invokeinterface(p(BasicBlock.class), "call", sig(Completion.class, ExecutionContext.class));
        // completion
        dup();
        // completion completion
        getfield(p(Completion.class), "type", ci(Completion.Type.class));
        // completion type
        invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
        // completion type
        ldc(Completion.Type.RETURN.ordinal());
        // completion type RETURN

        if_icmpeq(returnValue);
        // completion
        pop();
        // <empty>
        getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
        // UNDEF
        areturn();
        
        label(returnValue);
        // completion
        getfield(p(Completion.class), "value", ci(Object.class));
        // value
        areturn();
        
        
    }

}
