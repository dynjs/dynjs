package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Generic Add where we do not know enough about types to convert it from being a call.
 * We make it an Add vs Call so that if later we propagate a constant value and it can
 * simplify this to a call or a single operand we can more easily perform that
 * optimization.
 */
// FIXME: This should be merged into call when call is created
public class Add extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand lhs;
    private Operand rhs;
    private boolean subtract;

    // FIXME: This should not pass subtract in as it is invariant
    public Add(Variable result, Operand lhs, Operand rhs, boolean subtract) {
        super(Operation.ADD);
        this.result = result;
        this.lhs = lhs;
        this.rhs = rhs;
        this.subtract = subtract;
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

    public boolean isSubtraction() {
        return subtract;
    }

    public String toString() {
        return "" + result + " = " + lhs + " + " + rhs;
    }
}
