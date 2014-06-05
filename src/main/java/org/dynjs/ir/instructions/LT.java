package org.dynjs.ir.instructions;

import java.util.Map;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 */
public class LT extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand arg1;
    private Operand arg2;

    public LT(Variable result, Operand arg1, Operand arg2) {
        super(Operation.LT);
        this.result = result;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public void updateResult(Variable newResult) {
        this.result = newResult;
    }

    public void simplifyOperands(Map<Operand, Operand> renameMap, boolean force) {
        arg1 = arg1.getSimplifiedOperand(renameMap, force);
        arg2 = arg2.getSimplifiedOperand(renameMap, force);
    }

    public Operand[] getOperands() {
        return new Operand[] { result, arg1, arg2 };
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
        return "" + result + " = " + arg1 + " < " + arg2;
    }
}
