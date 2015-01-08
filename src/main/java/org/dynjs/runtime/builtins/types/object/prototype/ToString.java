package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.2
        if (self == Types.UNDEFINED) {
            return "[object Undefined]";
        }

        if (self == Types.NULL) {
            return "[object Null]";
        }

        JSObject o = Types.toObject(context, self);

        return "[object " + o.getClassName() + "]";
    }

}
