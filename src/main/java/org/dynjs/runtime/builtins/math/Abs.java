package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.Math;

public class Abs extends AbstractNativeFunction {

    public Abs(GlobalContext globalContext) {
        super(globalContext, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Math.coerceLongIfPossible(java.lang.Math.abs(Types.toNumber(context, args[0]).doubleValue()));
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Abs.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: abs>";
    }

}
