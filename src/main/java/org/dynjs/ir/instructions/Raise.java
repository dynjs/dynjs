package org.dynjs.ir.instructions;

import org.dynjs.ir.Instruction;
import org.dynjs.ir.Operation;

/**
 * Raise/throw an exception.
 */
public class Raise extends Instruction {
    private String type;
    private String message;

    public Raise(String type, String message) {
        super(Operation.RAISE);

        this.type = type;
        this.message = message;
    }

    public boolean transfersControl() {
        return true;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return "raise " + type + ", '" + message + "'";
    }
}
