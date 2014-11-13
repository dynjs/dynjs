package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.Math;

public class Round extends AbstractNativeFunction {
    
    public Round(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        final Double x = Types.toNumber(context, args[0]).doubleValue();
        if (x.isNaN() || x.isInfinite()) { return x; }
        return Math.coerceLongIfPossible(java.lang.Math.round(x));
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Round.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: round>";
    }

}
