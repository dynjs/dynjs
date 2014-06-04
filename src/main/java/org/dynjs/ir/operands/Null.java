package org.dynjs.ir.operands;

import java.util.List;
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
    public Object retrieve(ExecutionContext context, Object[] temps) {
        return Types.NULL;
    }

    public void addUsedVariables(List<Variable> l) {
    }

    public String toString() {
        return "%null";
    }
}
