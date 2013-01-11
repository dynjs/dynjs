package org.dynjs.runtime.interp;

import org.dynjs.runtime.BlockManager;

public class InterpretingVisitorFactory {
    
    private boolean invokeDynamicEnabled;

    public InterpretingVisitorFactory(boolean invokeDynamicEnabled) {
        this.invokeDynamicEnabled = invokeDynamicEnabled;
    }
    
    public InterpretingVisitor createVisitor(BlockManager blockManager) {
        if ( this.invokeDynamicEnabled ) {
            return new InvokeDynamicInterpretingVisitor(blockManager);
        } else {
            return new BasicInterpretingVisitor(blockManager);
        }
    }

}
