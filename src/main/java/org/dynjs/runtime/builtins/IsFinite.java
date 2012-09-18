package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class IsFinite extends AbstractNativeFunction {

    public IsFinite(GlobalObject globalObject) {
        super(globalObject, "text");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if (o != Types.UNDEFINED) {
            Number n = Types.toNumber(context, o);
            if (Double.isNaN(n.doubleValue()) || Double.isInfinite(n.doubleValue())) {
                return false;
            }
            return true;
        } else {
            return Types.UNDEFINED;
        }
    }
}
