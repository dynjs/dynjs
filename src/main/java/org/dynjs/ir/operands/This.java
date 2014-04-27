package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.ir.OperandType;
import org.dynjs.runtime.ExecutionContext;

/**
 * This binding for a scope
 */
public class This extends Operand {
    public static final This THIS = new This();

    protected This() {
        super(OperandType.THIS);
    }

    public String toString() {
        return "%this";
    }

    public Object retrieve(ExecutionContext context, Object[] temps) {
        return context.getThisBinding();
    }
}
