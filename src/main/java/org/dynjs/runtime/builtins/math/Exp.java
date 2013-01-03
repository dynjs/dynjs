package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Exp extends AbstractNativeFunction {

    public Exp(GlobalObject globalObject) {
        super(globalObject, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (DynNumber.isNaN(args[0])) {
            return Double.NaN;
        }
        final Double arg = Types.toNumber(context, args[0]).doubleValue();
        // These special cases handle return values that should not be floaty
        // according to the spec
        if (arg == 0) {
            return 1;
        } else if (Double.isInfinite(arg) && arg < 0) {
            return 0;
        }
        return java.lang.Math.exp(arg);
    }

}
