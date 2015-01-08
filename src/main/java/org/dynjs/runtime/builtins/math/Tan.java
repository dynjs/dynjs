package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.Math;

public class Tan extends AbstractNativeFunction {

    public Tan(GlobalContext globalContext) {
        super(globalContext, "x");
    }
    
    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        final Double arg = Types.toNumber(context, args[0]).doubleValue();
        if (arg.isInfinite() || arg.isNaN()) {
            return Double.NaN;
        }
        return Math.coerceLongIfPossible(java.lang.Math.tan(arg));
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Tan.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: tan>";
    }

}
