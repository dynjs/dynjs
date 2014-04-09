package org.dynjs.ir.operands;

import org.dynjs.ir.OperandType;

/**
 * Represents a variable which is located via an offset field.
 */
public abstract class OffsetVariable extends Variable {
    private int offset;

    public OffsetVariable(OperandType type, int offset) {
        super(type);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return getName() + "{" + offset + "}";
    }
}
