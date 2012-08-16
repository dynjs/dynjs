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
            if (o instanceof Number) { 
                return !Double.isInfinite( ((Number)o).doubleValue() ); 
            }
            return IsNaN.isNaN( context, args ) ? false : true;
        } else {
            return Types.UNDEFINED;
        }
    }
}
