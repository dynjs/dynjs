package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.Math;

public class Tan extends AbstractNativeFunction {

    public Tan(GlobalObject globalObject) {
        super(globalObject, "x");
    }
    
    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Math.coerceLongIfPossible(java.lang.Math.sqrt(Math.functionArgToDouble(context, args[0])));
    }

}
