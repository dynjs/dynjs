package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;

/**
 * Created by enebo on 5/3/14.
 */
public class Raise extends Instruction {
    private String message;

    public Raise(String message) {
        super(Operation.RAISE);

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
