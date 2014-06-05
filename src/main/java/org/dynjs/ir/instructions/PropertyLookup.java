package org.dynjs.ir.instructions;

import java.util.Map;
import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Variable;

/**
 */
public class PropertyLookup extends Instruction implements ResultInstruction {
    private Variable result;
    private Operand base;
    private String identifier;

    public PropertyLookup(Variable result, Operand base, String identifier) {
        super(Operation.PROPERTY_LOOKUP);
        this.result = result;
        this.base = base;
        this.identifier = identifier;
    }

    @Override
    public void updateResult(Variable newResult) {
        this.result = newResult;
    }

    public void simplifyOperands(Map<Operand, Operand> renameMap, boolean force) {
        base = base.getSimplifiedOperand(renameMap, force);
    }

    public Operand[] getOperands() {
        return new Operand[] { result, base };
    }

    @Override
    public Variable getResult() {
        return result;
    }

    public Operand getBase() {
        return base;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "" + result + " = " + base + "[" + identifier + "]";
    }
}
