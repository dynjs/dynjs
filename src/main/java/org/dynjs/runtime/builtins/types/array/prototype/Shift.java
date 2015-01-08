package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Shift extends AbstractNonConstructorFunction {

    public Shift(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.9
        JSObject o = Types.toObject(context, self);
        long len = Types.toUint32(context, o.get(context, "length"));

        if (len == 0) {
            o.put(context, "length", 0L, true);
            return Types.UNDEFINED;
        }

        Object first = o.get(context, "0");

        for (long k = 1; k < len; ++k) {
            boolean fromPresent = o.hasProperty(context, "" + k);

            if (fromPresent) {
                o.put(context, "" + (k - 1), o.get(context, "" + k), true);
            } else {
                o.delete(context, "" + (k - 1), true);
            }
        }

        o.delete(context, "" + (len - 1), true);
        o.put(context, "length", len - 1, true);

        return first;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Shift.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: shift>";
    }

}
