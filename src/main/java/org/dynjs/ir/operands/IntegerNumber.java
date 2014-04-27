package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

public class IntegerNumber extends Operand {
    private long value;

    public IntegerNumber(long value) {
        super(OperandType.INTEGER);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps) {
        return value;
    }

    @Override
    public String toString() {
        return "Integer:" + this.value;
    }
}
