package org.dynjs.runtime;

import org.dynjs.parser.Statement;

public abstract class AbstractBasicBlock implements BasicBlock {
    
    private Statement[] statements;

    public AbstractBasicBlock(Statement[] statements) {
        this.statements = statements;
    }

}
