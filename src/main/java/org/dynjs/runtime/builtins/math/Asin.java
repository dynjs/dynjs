package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Asin extends AbstractNativeFunction {
    
    public Asin(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Number arg = Types.toNumber(args[0]);
        if (arg instanceof Double) {
            Double argd = (Double) arg;
            if (argd > 1 || argd < -1 || Double.isNaN(argd)) {
                return Double.NaN;
            } else if (argd == 0) {
                return 0;
            }
            return Math.asin(argd);
        } else {
            if ((Integer) arg == 1) {
                return 0;
            }
            return (int) Math.asin((Integer) arg);
        }
    }

}
