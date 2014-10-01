package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class HasOwnProperty extends AbstractNativeFunction {

    public HasOwnProperty(GlobalContext globalContext) {
        super(globalContext, "v");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.5
        JSObject o = Types.toObject(context, self);
        String p = Types.toString(context, args[0]);

        Object desc = o.getOwnProperty(context, p, false);
        if (desc == Types.UNDEFINED) {
            return false;
        }

        return true;
    }

}
