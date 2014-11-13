package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ToLocaleString extends AbstractNativeFunction {

    public ToLocaleString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.3
        JSObject o = Types.toObject(context, self);

        Object toString = o.get(context, "toString");
        if (!(toString instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("toString must be callable"));
        }

        return context.call((JSFunction) toString, o);
    }

}
