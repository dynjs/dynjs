package org.dynjs.ir.instructions;

import java.util.Map;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

public class Copy extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand value;

    public Copy(Variable result, Operand value) {
        super(Operation.COPY);
        this.result = result;
        this.value = value;
    }

    @Override
    public void updateResult(Variable newResult) {
        this.result = newResult;
    }

    public void simplifyOperands(Map<Operand, Operand> renameMap, boolean force) {
        value = value.getSimplifiedOperand(renameMap, force);
    }

    public Operand[] getOperands() {
        return new Operand[] { result, value };
    }

    public Variable getResult() {
        return result;
    }

    public Operand getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + result + " = " + value + " (copy)";
    }
}
