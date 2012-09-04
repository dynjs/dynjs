package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class PropertyIsEnumerable extends AbstractNativeFunction {

    public PropertyIsEnumerable(GlobalObject globalObject) {
        super(globalObject, "v");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.7
        JSObject o = Types.toObject(context, self);
        String v = Types.toString(context, args[0]);

        Object desc = o.getOwnProperty(context, v);
        if (desc == Types.UNDEFINED) {
            return false;
        }

        Object isEnumerable = ((PropertyDescriptor) desc).get("Enumerable");

        if (isEnumerable instanceof Boolean) {
            return ((Boolean) isEnumerable).booleanValue();
        }

        return false;
    }

}
