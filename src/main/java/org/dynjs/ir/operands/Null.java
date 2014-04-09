package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

public class Null extends Operand {

    public static final Operand NULL = new Null();

    protected Null() {
        super(OperandType.NULL);
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return Types.NULL;
    }

    public String toString() {
        return "%null";
    }
}
