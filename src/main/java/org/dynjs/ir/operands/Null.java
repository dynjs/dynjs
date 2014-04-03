package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.runtime.Types;

public class Null extends Operand {

    public static final Operand NULL = new Null();

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return Types.NULL;
    }

    public String toString() {
        return "%null";
    }
}
