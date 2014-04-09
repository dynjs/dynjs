package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

/**
 * Represent a literal 'true' or 'false'
 */
public class BooleanLiteral extends Operand {
    public static BooleanLiteral TRUE = new BooleanLiteral(true);
    public static BooleanLiteral FALSE = new BooleanLiteral(false);

    private boolean truth;

    public BooleanLiteral(boolean truth) {
        super(OperandType.BOOLEAN);
        this.truth = truth;
    }

    public String toString() {
        return "" + truth;
    }

    @Override
    public Object retrieve(ExecutionContext context, Object[] temps, Object[] vars) {
        return truth;
    }
}
