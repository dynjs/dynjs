package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

/**
 * Represent a literal 'true' or 'false'
 */
public class BooleanLiteral extends Operand {
    public static BooleanLiteral TRUE = new BooleanLiteral(true);
    public static BooleanLiteral FALSE = new BooleanLiteral(false);

    private boolean truth;

    public BooleanLiteral(boolean truth) {
        this.truth = truth;
    }

    public String toString() {
        return "" + truth;
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return truth;
    }
}
