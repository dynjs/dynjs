package org.dynjs.ir.instructions;

import org.dynjs.ir.operands.Variable;

/**
 * Any instruction which returns a result to a variable.
 */
public interface ResultInstruction {
    public Variable getResult();
    public void updateResult(Variable newResult);
}
