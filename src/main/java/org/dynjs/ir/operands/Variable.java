package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;

public abstract class Variable extends Operand {
    protected Variable(OperandType type) {
        super(type);
    }

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }
}
