package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Label;

public abstract class Branch extends Instruction {
    private Label target;

    public Branch(Operation operation, Label target) {
        super(operation);
        this.target = target;
    }

    public Label getTarget() {
        return target;
    }

    @Override
    public boolean transfersControl() {
        return true;
    }
}
