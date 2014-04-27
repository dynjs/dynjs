package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 * Explicitly receive argument 'n' from a function.  When no 'arguments' usage and we
 * can see no usages of the variable this is assigned to we can eliminate it via
 * DCE.
 */
public class ReceiveFunctionParameter extends Instruction implements ResultInstruction {
    private Variable result;
    private int offset;

    public ReceiveFunctionParameter(Variable result, int offset) {
        super(Operation.RECEIVE_FUNCTION_PARAM);
        this.result = result;
        this.offset = offset;
    }

    public int getIndex() {
        return offset;
    }

    @Override
    public Variable getResult() {
        return result;
    }

    public String toString() {
        return "" + getResult() + " = receive_parameter(" + offset + ")";
    }
}
