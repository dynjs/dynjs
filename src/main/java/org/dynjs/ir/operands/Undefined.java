package org.dynjs.ir.operands;

import org.dynjs.ir.Operand;
import org.dynjs.runtime.Types;

/**
 * Created by enebo on 3/29/14.
 */
public class Undefined extends Operand {
    public static final Operand UNDEFINED = new Undefined();

    @Override
    public Object retrieve(Object[] temps, Object[] vars) {
        return Types.UNDEFINED;
    }
}
