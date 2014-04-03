package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public abstract class Variable extends Operand {
    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }
}
