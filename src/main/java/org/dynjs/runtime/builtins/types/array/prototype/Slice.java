package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;

public class Slice extends AbstractNonConstructorFunction {

    public Slice(GlobalObject globalObject) {
        super(globalObject, "start", "end");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.10
        JSObject o = Types.toObject(context, self);

        JSObject a = BuiltinArray.newArray(context);
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
                PropertyDescriptor desc = new PropertyDescriptor();
                desc.set(Names.VALUE, kValue);
                desc.set(Names.WRITABLE, true);
                desc.set(Names.CONFIGURABLE, true);
                desc.set(Names.ENUMERABLE, true);
                a.defineOwnProperty(context, "" + n, desc, false);
            }
            ++k;
            ++n;
        }

        return a;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Slice.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: slice>";
    }
}
