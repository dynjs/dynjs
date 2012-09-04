package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Atan extends AbstractNativeFunction {
    
    public Atan(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Number arg = Types.toNumber(context, args[0]);
        if (arg instanceof Double) {
            return Math.atan((Double) arg);
        } else {
            return (int) Math.atan((Integer) arg);
        }
    }
}
