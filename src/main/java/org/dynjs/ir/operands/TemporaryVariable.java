package org.dynjs.ir.operands;

public class TemporaryVariable extends Variable {
    private int offset;

    public TemporaryVariable(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String getName() {
        return "%t_" + offset;
    }
}
