package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ReduceRight extends AbstractNativeFunction {

    public ReduceRight(GlobalContext globalContext) {
        super(globalContext, "callbackFn");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.22
        JSObject o = Types.toObject(context, self);
        long len = Types.toUint32(context, o.get(context, "length"));

        if (!(args[0] instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("callbackFn must be a function"));
        }

        JSFunction callbackFn = (JSFunction) args[0];

        if (len == 0 && args.length < 2) {
            throw new ThrowException(context, context.createTypeError("length is 0 and no initial value provided"));
        }

        Object accumulator = null;

        long k = len - 1;
        if (args.length >= 2) {
            accumulator = args[1];
        } else {
            boolean kPresent = false;

            while (k >= 0 && !kPresent) {
                kPresent = o.hasProperty(context, "" + k);
                if (kPresent) {
                    accumulator = o.get(context, "" + k);
                }
                --k;
            }

            if (!kPresent) {
                throw new ThrowException(context, context.createTypeError("no initial value and no first value in array"));
            }
        }

        while (k >= 0) {
            if (o.hasProperty(context, "" + k)) {
                final Object kValue = o.get(context, "" + k);
                accumulator = context.call(callbackFn, Types.UNDEFINED, accumulator, kValue, k, o);
            }
            --k;
        }

        return accumulator;

    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/ReduceRight.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: reduceRight>";
    }

}
