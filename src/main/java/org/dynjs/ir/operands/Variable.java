package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public abstract class Variable extends Operand {
    private int offset;

    public Variable(int offset) {
        this.offset = offset;
    }

    public abstract String getName();

    public int getOffset() {
        return offset;
    }
}
