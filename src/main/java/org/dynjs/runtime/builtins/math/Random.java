package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;

public class Random extends AbstractNativeFunction {
    
    public Random(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Math.random();
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/math/Random.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: random>";
    }
}
