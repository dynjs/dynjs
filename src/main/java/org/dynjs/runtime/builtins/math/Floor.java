package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Floor extends AbstractNativeFunction {

    public Floor(GlobalContext globalContext) {
        super(globalContext, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (DynNumber.isNaN(args[0])) {
            return Double.NaN;
        }
        final Double arg = Types.toNumber(context, args[0]).doubleValue();
        if (Double.isInfinite(arg)) {
            return arg;
        }
        return (long) java.lang.Math.floor(arg);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Floor.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: floor>";
    }

}
