package org.dynjs.parser.ast;

import org.dynjs.parser.Statement;

public abstract class PropertyAccessor extends PropertyAssignment {

    private Statement block;

    public PropertyAccessor(String name, Statement block) {
        super(name);
        this.block = block;
    }

    public Statement getBlock() {
        return this.block;
    }
    
    public int getSizeMetric() {
        return this.block.getSizeMetric() + 3;
    }

}
