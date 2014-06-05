package org.dynjs.ir.operands;

import java.util.List;
import java.util.Map;
import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;

public abstract class Variable extends Operand {
    protected Variable(OperandType type) {
        super(type);
    }

    public abstract String getName();

    public void addUsedVariables(List<Variable> l) {
        l.add(this);
    }

    @Override
    public Operand getSimplifiedOperand(Map<Operand, Operand> valueMap, boolean force) {
        Operand newOperand = valueMap.get(this);
        // You can only value-replace atomic values
        return newOperand != null && force ? newOperand : this;
    }

    @Override
    public String toString() {
        return getName();
    }
}
