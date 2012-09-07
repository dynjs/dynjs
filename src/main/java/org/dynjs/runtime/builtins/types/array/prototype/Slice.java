package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;

public class Slice extends AbstractNativeFunction {

    public Slice(GlobalObject globalObject) {
        super(globalObject, "start", "end");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.10
        JSObject o = Types.toObject(context, self);

        DynArray a = BuiltinArray.newArray(context);
        long len = Types.toUint32(context, o.get(context, "length"));

        long relativeStart = Types.toInteger(context, args[0]);
        long k = Math.min(relativeStart, len);

        if (relativeStart < 0) {
            k = Math.max(len + relativeStart, 0);
        }

        long relativeEnd = len;
        if (args[1] != Types.UNDEFINED) {
            relativeEnd = Types.toInteger(context, args[1]);
        }

        long finalPos = Math.min(relativeEnd, len);

        if (relativeEnd < 0) {
            finalPos = Math.max(len + relativeEnd, 0);
        }

        long n = 0;

        while (k < finalPos) {
            boolean kPresent = o.hasProperty(context, "" + k);
            if (kPresent) {
                final Object kValue = o.get(context, "" + k);
                a.defineOwnProperty(context, "" + n, new PropertyDescriptor() {
                    {
                        set("Value", kValue);
                        set("Writable", true);
                        set("Configurable", true);
                        set("Enumerable", true);
                    }
                }, false);
            }
            ++k;
            ++n;
        }

        return a;
    }
}
