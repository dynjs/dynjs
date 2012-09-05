package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Ceil extends AbstractNativeFunction {

    public Ceil(GlobalObject globalObject) {
        super(globalObject, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (DynNumber.isNaN(args[0])) {
            return Double.NaN;
        }
        final Double arg = new Double(Types.toNumber(context, args[0]).toString());
        if (Double.isInfinite(arg)) {
            return arg;
        }
        return (int) Math.ceil(arg);
    }

}
