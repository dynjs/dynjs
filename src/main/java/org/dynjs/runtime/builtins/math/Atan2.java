package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.Math;
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
        Double y = Math.functionArgToDouble(context, args[0]);
        Double x = Math.functionArgToDouble(context, args[1]);
        if (x == -0 && y == -0) { return -java.lang.Math.PI; }
        return java.lang.Math.atan2(y, x);
    }

}
