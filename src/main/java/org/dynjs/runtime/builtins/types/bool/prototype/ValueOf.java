package org.dynjs.runtime.builtins.types.bool.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.bool.DynBoolean;

public class ValueOf extends AbstractNativeFunction {
    public ValueOf(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.6.4.3
        if (self instanceof DynBoolean) {
            return ((DynBoolean) self).getPrimitiveValue();
        }
        if (self instanceof Boolean) {
            return self;
        }
        throw new ThrowException(context, context.createTypeError("not a boolean"));
    }
}
