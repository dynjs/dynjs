package org.dynjs.runtime.interp;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.BlockManager;

public class InterpretingVisitorFactory {
    
    private boolean invokeDynamicEnabled;

    public InterpretingVisitorFactory(boolean invokeDynamicEnabled) {
        this.invokeDynamicEnabled = invokeDynamicEnabled;
    }
    
    public CodeVisitor createVisitor(BlockManager blockManager) {
        if ( this.invokeDynamicEnabled ) {
            return new InvokeDynamicInterpretingVisitor(blockManager);
        } else {
            return new BasicInterpretingVisitor(blockManager);
        }
    }

}
