package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;
import org.dynjs.ir.operands.Label;

public class ExceptionRegionStartMarker extends Instruction {
    private Label label;

    public ExceptionRegionStartMarker(Label label) {
        super(Operation.EXCEPTION_REGION_START);
        this.label = label;
    }

    public Operand[] getOperands() {
        return new Operand[] { };
    }

    public Label getLabel() {
        return label;
    }
}
