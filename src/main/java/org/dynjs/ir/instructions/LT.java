package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.operands.Variable;

/**
 */
public class LT extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand arg1;
    private Operand arg2;

    public LT(Variable result, Operand arg1, Operand arg2) {
        this.result = result;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public Variable getResult() {
        return null;
    }

    public Operand getArg1() {
        return arg1;
    }

    public Operand getArg2() {
        return arg2;
    }

    public String toString() {
        return "" + arg1 + " < " + arg2;
    }
}
