package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Some extends AbstractNativeFunction {

    public Some(GlobalObject globalObject) {
        super(globalObject, "callbackFn");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.17
        JSObject o = Types.toObject(context, self);
        int len = Types.toUint32(context, o.get(context, "length"));

        if (!(args[0] instanceof JSFunction)) {
            throw new ThrowException(context.createTypeError("callbackFn must be a function"));
        }

        JSFunction callbackFn = (JSFunction) args[0];

        Object t = Types.UNDEFINED;
        if (args.length > 1) {
            t = args[1];
        }

        for (int k = 0; k < len; ++k) {
            boolean kPresent = o.hasProperty(context, "" + k);
            if (kPresent) {
                Object kValue = o.get(context, "" + k);
                Object result = context.call(callbackFn, t, kValue, k, o);
                if ( Types.toBoolean(result) == Boolean.TRUE ) {
                    return true;
                }
            }
        }

        return false;
    }

}
