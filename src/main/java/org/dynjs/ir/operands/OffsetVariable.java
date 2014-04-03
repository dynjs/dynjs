package org.dynjs.ir.operands;

/**
 * Represents a variable which is located via an offset field.
 */
public abstract class OffsetVariable extends Variable {
    private int offset;

    public OffsetVariable(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return getName() + "(" + offset + ")";
    }
}
