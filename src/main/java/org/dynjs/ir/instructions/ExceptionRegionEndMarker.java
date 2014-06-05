package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;

public class ExceptionRegionEndMarker extends Instruction {
    public ExceptionRegionEndMarker() {
        super(Operation.EXCEPTION_REGION_END);
    }

    public Operand[] getOperands() {
        return new Operand[] { };
    }
}
