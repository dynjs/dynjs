package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.Math;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Max extends AbstractNativeFunction {

    public Max(GlobalContext globalContext) {
        super(globalContext, "value1", "value2");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // No arguments supplied
        if (args[0] == Types.UNDEFINED) {
            return Double.NEGATIVE_INFINITY;
        }
        // One argument supplied
        if (args[1] == Types.UNDEFINED) {
            return args[0];
        }
        // One NaN argument supplied
        if (DynNumber.isNaN(args[0])) {
            return Double.NaN;
        }

        double max  = new Double(Types.toNumber(context, args[0]).toString());

        for (int i = 1; i < args.length; i++) {
            if (DynNumber.isNaN(args[i]))
                return Double.NaN;
            max = java.lang.Math.max(Types.toNumber(context, args[i]).doubleValue(), max);
        }
        return Math.coerceLongIfPossible(max);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Max.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: max>";
    }

}
