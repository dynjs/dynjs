package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Every extends AbstractNativeFunction {

    public Every(GlobalContext globalContext) {
        super(globalContext, "callbackFn");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.16
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

        for (long k = 0; k < len; ++k) {
            boolean kPresent = o.hasProperty(context, "" + k);
            if (kPresent) {
                Object kValue = o.get(context, "" + k);
                Object result = context.call(callbackFn, t, kValue, k, o);
                if ( Types.toBoolean(result) == Boolean.FALSE ) {
                    return false;
                }
            }
        }

        return true;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Every.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: every>";
    }

}
