package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;

public class ToJSON extends AbstractDateFunction {

    public ToJSON(GlobalObject globalObject) {
        super(globalObject, "key");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        JSObject o = Types.toObject(context, self);
        Object tv = Types.toPrimitive(context, o, "Number");

        if (tv instanceof Number) {
            if (Double.isInfinite(((Number) tv).doubleValue())) {
                return Types.NULL;
            }
        }

        Object toISO = o.get(context, "toISOString");
        if (!(toISO instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("toISOString must be a function"));
        }

        return context.call((JSFunction) toISO, o);

    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/ToJSON.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: toJSON>";
    }

}
