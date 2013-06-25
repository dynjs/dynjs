package org.dynjs.runtime.builtins.math;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

public class Random extends AbstractNativeFunction {
    
    public Random(GlobalObject globalObject) {
        super(globalObject);
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
