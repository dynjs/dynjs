package org.dynjs.runtime;

import org.dynjs.parser.statement.BlockStatement;

public abstract class AbstractBasicBlock implements BasicBlock {
    
    private BlockStatement body;

    public AbstractBasicBlock(BlockStatement body) {
        this.body = body;
    }

}
