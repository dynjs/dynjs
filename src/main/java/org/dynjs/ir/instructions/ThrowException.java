package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operand;
import org.dynjs.ir.Operation;

/**
 * Created by enebo on 3/31/14.
 */
public class ThrowException extends Instruction {
    public ThrowException() {
        super(Operation.THROW_EXCEPTION);
    }

    public Operand[] getOperands() {
        return new Operand[] {};
    }
}
