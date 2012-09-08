package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.joda.time.DateTime;

public class ToTimeString extends AbstractNativeFunction {
    public ToTimeString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return new DateTime((Long) ((DynDate) self).getPrimitiveValue()).toString("HH:mm:ss 'GMT'Z (z)");
    }
}
