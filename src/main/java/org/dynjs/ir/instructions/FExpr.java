package org.dynjs.ir.instructions;

import org.dynjs.ir.IRJSFunction;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;

public class FExpr extends Instruction {
    private final IRJSFunction function;

    public FExpr(IRJSFunction function) {
        super(Operation.FEXPR);
        this.function = function;
    }

    @Override
    public String toString() {
        return "(function())";
    }
}
