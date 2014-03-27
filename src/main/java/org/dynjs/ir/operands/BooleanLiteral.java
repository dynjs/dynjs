package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

/**
 * Represent a literal 'true' or 'false'
 */
public class BooleanLiteral extends Operand {
    private boolean truth;

    public BooleanLiteral(boolean truth) {
        this.truth = truth;
    }

    public String toString() {
        return "" + truth;
    }
}
