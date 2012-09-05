package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Max extends AbstractNativeFunction {
    
    public Max(GlobalObject globalObject) {
        super(globalObject, "value1", "value2");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // No arguments supplied
        if (args[0] == Types.UNDEFINED) { return Double.NaN; }
        // One argument supplied
        if (args[1] == Types.UNDEFINED) { return args[0]; }
        double max = 0;
        for (int i = 1; i < args.length; i++) {
            if (DynNumber.isNaN(args[i]) || DynNumber.isNaN(args[i-1])) return Double.NaN;
            max = Math.max(new Double(Types.toNumber(context, args[i]).toString()), new Double(Types.toNumber(context, args[i-1]).toString()));
        }
        if (max - Math.floor(max) == 0) return (int) max; 
        else return max;
    }

}
