package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.operands.Variable;

public class Copy extends Instruction {
    private Variable result;
    private Operand value;

    public Copy(Variable result, Operand value) {
        this.result = result;
        this.value = value;
    }

    public Variable getResult() {
        return result;
    }

    public Operand getValue() {
        return value;
    }

    @Override
    public String toString() {
        return result + " = " + value;
    }
}
