package org.dynjs.ir.operands;

import java.util.List;
import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

public class FloatNumber extends Operand {
    private double value;

    public FloatNumber(double value) {
        super(OperandType.FLOAT);
        this.value = value;
    }

    public void addUsedVariables(List<Variable> l) {
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
