package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class IsFinite extends AbstractNativeFunction {

    public IsFinite(GlobalObject globalObject) {
        super( globalObject, "text" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if (o != Types.UNDEFINED) {
            if (o.getClass() == Integer.class || o.getClass() == Double.class) { return true; }
            return IsNaN.isNaN( args ) ? false : true;
        } else {
            return Types.UNDEFINED;
        }
    }
}
