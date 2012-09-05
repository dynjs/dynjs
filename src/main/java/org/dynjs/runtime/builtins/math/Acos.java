package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Acos extends AbstractNativeFunction {

    public Acos(GlobalObject globalObject) {
        super(globalObject, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Number arg = Types.toNumber(context, args[0]);
        if (arg instanceof Double) {
            Double argd = (Double) arg;
            if (argd > 1 || argd < -1 || Double.isNaN(argd)) {
                return Double.NaN;
            }
            return Math.acos(argd);
        } else {
            return (int) Math.acos((Integer) arg);
        }
    }

}
