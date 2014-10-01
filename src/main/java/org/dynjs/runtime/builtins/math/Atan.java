package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.Math;

public class Atan extends AbstractNativeFunction {

    public Atan(GlobalContext globalContext) {
        super(globalContext, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Math.coerceLongIfPossible(java.lang.Math.atan(Types.toNumber(context, args[0]).doubleValue()));
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Atan.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: atan>";
    }
}
