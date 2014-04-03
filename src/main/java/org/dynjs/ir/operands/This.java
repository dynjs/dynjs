package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

/**
 * This binding for a scope
 */
public class This extends Operand {
    public static final This THIS = new This();

    public String toString() {
        return "%this";
    }
}
