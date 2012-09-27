package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class ValueOf extends AbstractNativeFunction {

    public ValueOf(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.4
        JSObject o = Types.toObject(context, self);
        return o;
    }

}
