package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public class IntegerNumber extends Operand {
    private long value;

    public IntegerNumber(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return value;
    }
}
