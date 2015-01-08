package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class IsPrototypeOf extends AbstractNativeFunction {

    public IsPrototypeOf(GlobalContext globalContext) {
        super(globalContext, "v");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.6
        Object v = args[0];
        if (!(v instanceof JSObject)) {
            return false;
        }

        JSObject o = Types.toObject(context, self);

        while (true) {
            v = ((JSObject) v).getPrototype();
            if (v == null) {
                return false;
            }
            if (v == o) {
                return true;
            }
        }
    }

}
