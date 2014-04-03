package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.operands.Label;

/**
 * Created by enebo on 3/31/14.
 */
public class Jump extends Instruction {
    private Label target;

    public Jump(Label target) {
        this.target = target;
    }

    public Label getTarget() {
        return target;
    }

    public String toString() {
        return "jump -> " + target;
    }
}
