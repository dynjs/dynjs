package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public class FloatNumber extends Operand {
    private double value;

    public FloatNumber(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return value;
    }

    @Override
    public String toString() {
        return "Float:" + this.value;
    }
}
