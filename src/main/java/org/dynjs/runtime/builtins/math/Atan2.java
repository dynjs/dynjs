package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Atan2 extends AbstractNativeFunction {

    public Atan2(GlobalObject globalObject) {
        // Don't fix this. By convention, atan2 has the y variable first
        super(globalObject, "y", "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (DynNumber.isNaN(args[0]) || DynNumber.isNaN(args[1])) {
            return Double.NaN;
        }
        Double y = new Double(Types.toNumber(context, args[0]).toString());
        Double x = new Double(Types.toNumber(context, args[1]).toString());
        return Math.atan2(y, x);
    }

}
