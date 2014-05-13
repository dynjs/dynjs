package org.dynjs.ir.instructions;

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
    public Variable getResult() {
        return result;
    }

    public Operand getBase() {
        return base;
    }

    public String getIdentifier() {
        return identifier;
    }
}
