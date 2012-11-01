package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ToString extends AbstractNonConstructorFunction {

    public ToString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        JSObject array = Types.toObject(context, self);
        Object func = array.get(context, "join");
        if (!Types.isCallable(func)) {
            func = context.getPrototypeFor("Object").get(context, "toString");
        }
        return context.call((JSFunction) func, array);
    }

}
