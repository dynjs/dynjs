package org.dynjs.ir.operands;

import org.dynjs.ir.IRJSFunction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

public class Fn extends Operand {
    private final IRJSFunction function;

    public Fn(IRJSFunction function) {
        super(OperandType.FUNCTION);
        this.function = function;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps) {
        return this.function;
    }
}
