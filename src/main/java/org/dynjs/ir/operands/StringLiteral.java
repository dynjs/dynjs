package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;

public class StringLiteral extends Operand {
    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return this.value;
    }
}
