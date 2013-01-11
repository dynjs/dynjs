package org.dynjs.codegen;

import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;

public class CodeGeneratingVisitorFactory {
    
    private boolean enableInvokeDynamic;

    public CodeGeneratingVisitorFactory(boolean enableInvokeDynamic) {
        this.enableInvokeDynamic = enableInvokeDynamic;
    }
    
    public CodeGeneratingVisitor create(BlockManager blockManager) {
        InterpretingVisitorFactory interpFactory = new InterpretingVisitorFactory( enableInvokeDynamic );
        if ( enableInvokeDynamic ) {
            return new InvokeDynamicBytecodeGeneratingVisitor( interpFactory, blockManager );
        }
        
        return new BasicBytecodeGeneratingVisitor( interpFactory, blockManager );
    }

}
