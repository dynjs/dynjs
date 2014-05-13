package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Label;

public class LabelInstr extends Instruction {
    private Label label;

    public LabelInstr(Label label) {
        super(Operation.LABEL);
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }
}
