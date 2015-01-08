package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Push extends AbstractNonConstructorFunction {

    public Push(GlobalContext globalContext) {
        super(globalContext, "item");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        JSObject array = Types.toObject(context, self);
        long n = Types.toUint32(context, array.get(context, "length"));

        for (Object each : args) {
            if (each != Types.UNDEFINED) {
                array.put(context, "" + n, each, true);
                ++n;
            }
        }

        array.put(context, "length", n, true);

        return n;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Push.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: push>";
    }

}
