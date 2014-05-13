package org.dynjs.ir.instructions;

import org.dynjs.ir.FunctionScope;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

public class DefineFunction extends Instruction implements ResultInstruction {
    private Variable result;
    private final FunctionScope function;

    public DefineFunction(Variable result, FunctionScope function) {
        super(Operation.DEFINE_FUNCTION);

        this.result = result;
        this.function = function;
    }

    public Variable getResult() {
        return result;
    }

    public FunctionScope getScope() {
        return function;
    }

    @Override
    public String toString() {
        return "" + result + " = function()";
    }
}
