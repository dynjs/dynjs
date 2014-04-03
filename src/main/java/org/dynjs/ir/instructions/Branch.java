package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.operands.Label;

public abstract class Branch extends Instruction {
    private Label target;

    public Branch(Label target) {
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
