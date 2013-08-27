package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;

public class Filter extends AbstractNativeFunction {

    public Filter(GlobalObject globalObject) {
        super(globalObject, "callbackFn");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.20
        JSObject o = Types.toObject(context, self);
        long len = Types.toUint32(context, o.get(context, "length"));

        if (!(args[0] instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("callbackFn must be a function"));
        }

        JSFunction callbackFn = (JSFunction) args[0];

        Object t = Types.UNDEFINED;
        if (args.length > 1) {
            t = args[1];
        }

        JSObject a = BuiltinArray.newArray(context);

        int to = 0;

        for (long k = 0; k < len; ++k) {
            boolean kPresent = o.hasProperty(context, "" + k);
            if (kPresent) {
                final Object kValue = o.get(context, "" + k);
                Object selected = context.call(callbackFn, t, kValue, k, o);

                if (Types.toBoolean(selected)) {
                    PropertyDescriptor desc = new PropertyDescriptor();
                    desc.set(Names.VALUE, kValue);
                    desc.set(Names.WRITABLE, true);
                    desc.set(Names.CONFIGURABLE, true);
                    desc.set(Names.ENUMERABLE, true);
                    a.defineOwnProperty(context, "" + to, desc, false);
                    ++to;
                }
            }
        }

        return a;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Filter.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: filter>";
    }
}
