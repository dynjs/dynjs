package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Label;

/**
 * Created by enebo on 3/31/14.
 */
public class Jump extends Instruction {
    private Label target;

    public Jump(Label target) {
        super(Operation.JUMP);
        this.target = target;
    }

    public boolean transfersControl() {
        return true;
    }

    public Label getTarget() {
        return target;
    }

    public String toString() {
        return "jump -> " + target;
    }
}
