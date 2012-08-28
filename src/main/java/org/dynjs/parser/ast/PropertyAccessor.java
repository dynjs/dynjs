package org.dynjs.parser.ast;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;

public abstract class PropertyAccessor extends PropertyAssignment {

    private BlockManager blockManager;
    private Statement block;

    public PropertyAccessor(BlockManager blockManager, String name, Statement block) {
        super(name);
        this.blockManager = blockManager;
        this.block = block;
    }
    
    public BlockManager getBlockManager() {
        return this.blockManager;
    }
    
    public Statement getBlock() {
        return this.block;
    }


}
