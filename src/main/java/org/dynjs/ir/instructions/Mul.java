package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Generic Mul where we do not know enough about types to convert it from being a call.
 * We make it an Mul vs Call so that if later we propagate a constant value and it can
 * simplify this to a call or a single operand we can more easily perform that
 * optimization.
 */
// FIXME: This should be merged into call when call is created
public class Mul extends Instruction {
    private Variable result;
    private Operand lhs;
    private Operand rhs;

    public Mul(Variable result, Operand lhs, Operand rhs) {
        super(Operation.MUL);
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
