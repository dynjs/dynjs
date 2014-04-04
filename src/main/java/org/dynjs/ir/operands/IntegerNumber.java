package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.runtime.ExecutionContext;

public class IntegerNumber extends Operand {
    private long value;

    public IntegerNumber(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return value;
    }

    @Override
    public String toString() {
        return "Integer:" + this.value;
    }
}
