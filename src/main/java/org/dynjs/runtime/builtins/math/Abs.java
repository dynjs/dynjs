package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Abs extends AbstractNativeFunction {

    public Abs(GlobalObject globalObject) {
        super(globalObject, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Number arg = Types.toNumber(context, args[0]);
        if (arg instanceof Double) {
            if (Double.isNaN((Double) arg)) {
                return Double.NaN;
            } else if (Double.isInfinite((Double) arg)) {
                return Double.POSITIVE_INFINITY;
            }
            return Math.abs((Double) arg);
        } else {
            return Math.abs((Integer) arg);
        }
    }

}
