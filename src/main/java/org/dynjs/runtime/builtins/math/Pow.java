package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.Math;

public class Pow extends AbstractNativeFunction {

    public Pow(GlobalObject globalObject) {
        super(globalObject, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Double x = Math.functionArgToDouble(context, args[0]);
        Double y = Math.functionArgToDouble(context, args[1]);
        return Math.coerceIntegerIfPossible(java.lang.Math.pow(x, y));
    }

}
