package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
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
        Double y = Types.toNumber(context, args[0]).doubleValue();
        Double x = Types.toNumber(context, args[1]).doubleValue();
        if (x.equals(-0.0) && y.equals(-0.0)) { return -java.lang.Math.PI; }
        return java.lang.Math.atan2(y, x);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Atan2.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: atan2>";
    }

}
