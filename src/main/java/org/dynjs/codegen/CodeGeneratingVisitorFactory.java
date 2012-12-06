package org.dynjs.codegen;

import org.dynjs.runtime.BlockManager;

public class CodeGeneratingVisitorFactory {
    
    private boolean enableInvokeDynamic;

    public CodeGeneratingVisitorFactory(boolean enableInvokeDynamic) {
        this.enableInvokeDynamic = enableInvokeDynamic;
    }
    
    public CodeGeneratingVisitor create(BlockManager blockManager) {
        if ( enableInvokeDynamic ) {
            return new InvokeDynamicBytecodeGeneratingVisitor( blockManager );
        }
        
        return new BasicBytecodeGeneratingVisitor( blockManager );
    }

}
