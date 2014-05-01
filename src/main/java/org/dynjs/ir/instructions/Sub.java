package org.dynjs.ir.instructions;

import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Generic Add where we do not know enough about types to convert it from being a call.
 * We make it an Add vs Call so that if later we propagate a constant value and it can
 * simplify this to a call or a single operand we can more easily perform that
 * optimization.
 */
public class Sub extends BinaryInstr {
    public Sub(Variable result, Operand lhs, Operand rhs) {
        super(Operation.SUB, result, lhs, rhs);
    }

    @Override
    public String toString() {
        return "" + getResult() + " = " + getLHS() + " - " + getRHS();
    }
}
