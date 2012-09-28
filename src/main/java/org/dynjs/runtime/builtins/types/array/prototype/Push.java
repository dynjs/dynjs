package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Push extends AbstractNativeFunction {

    public Push(GlobalObject globalObject) {
        super(globalObject, "item");
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

        return n;
    }

}
