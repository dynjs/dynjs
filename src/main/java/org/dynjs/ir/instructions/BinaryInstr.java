package org.dynjs.ir.instructions;

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
