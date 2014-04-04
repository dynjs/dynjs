package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.runtime.ExecutionContext;

public class FloatNumber extends Operand {
    private double value;

    public FloatNumber(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return value;
    }

    @Override
    public String toString() {
        return "Float:" + this.value;
    }
}
