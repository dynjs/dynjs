package org.dynjs.parser.ast;

import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;

public abstract class PropertyAccessor extends PropertyAssignment {

    private Statement block;

    public PropertyAccessor(Position position, String name, Statement block) {
        super(position, name);
        this.block = block;
    }

    public Statement getBlock() {
        return this.block;
    }
    
    public int getSizeMetric() {
        return this.block.getSizeMetric() + 3;
    }

}
