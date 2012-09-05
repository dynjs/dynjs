package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Pow extends AbstractNativeFunction {
    
    public Pow(GlobalObject globalObject) {
        super(globalObject, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Math.pow(new Double(Types.toNumber(context, args[0]).toString()), new Double(Types.toNumber(context, args[1]).toString()));
    }

}
