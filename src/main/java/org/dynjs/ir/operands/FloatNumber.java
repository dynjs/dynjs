package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

public class FloatNumber extends Operand {
    private double value;

    public FloatNumber(double value) {
        super(OperandType.FLOAT);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps) {
        return value;
    }

    @Override
    public String toString() {
        return "Float:" + this.value;
    }
}
