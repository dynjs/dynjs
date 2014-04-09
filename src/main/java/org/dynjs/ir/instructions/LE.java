package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

public class LE extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand arg1;
    private Operand arg2;

    public LE(Variable result, Operand arg1, Operand arg2) {
        super(Operation.LE);
        this.result = result;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public Variable getResult() {
        return result;
    }

    public Operand getArg1() {
        return arg1;
    }

    public Operand getArg2() {
        return arg2;
    }

    public String toString() {
        return "" + result + " = " + arg1 + " <= " + arg2;
    }
}
