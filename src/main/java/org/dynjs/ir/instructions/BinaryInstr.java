package org.dynjs.ir.instructions;

import java.util.Map;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Common bits for binary operations which set a result (a = b + c).
 */
public abstract class BinaryInstr extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand lhs;
    private Operand rhs;

    public BinaryInstr(Operation operation, Variable result, Operand lhs, Operand rhs) {
        super(operation);
        this.result = result;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void updateResult(Variable newResult) {
        this.result = newResult;
    }

    public void simplifyOperands(Map<Operand, Operand> valueMap, boolean force) {
        lhs = lhs.getSimplifiedOperand(valueMap, force);
        rhs = rhs.getSimplifiedOperand(valueMap, force);
    }

    public Operand[] getOperands() {
        return new Operand[] { result, lhs, rhs };
    }

    public Variable getResult() {
        return result;
    }

    public Operand getLHS() {
        return lhs;
    }

    public Operand getRHS() {
        return rhs;
    }
}
