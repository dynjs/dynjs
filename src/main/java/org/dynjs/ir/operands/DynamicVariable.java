package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

/**
 * A variable in which we do not know it's location.
 */
public class DynamicVariable extends Variable {
    private String name;

    public DynamicVariable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
