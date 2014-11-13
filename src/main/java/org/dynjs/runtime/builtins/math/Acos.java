package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.Math;

public class Acos extends AbstractNativeFunction {

    public Acos(GlobalContext globalContext) {
        super(globalContext, "x");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Math.coerceLongIfPossible(java.lang.Math.acos(Types.toNumber(context, args[0]).doubleValue()));
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Acos.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: acos>";
    }

}
