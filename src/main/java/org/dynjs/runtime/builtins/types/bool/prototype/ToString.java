package org.dynjs.runtime.builtins.types.bool.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.bool.DynBoolean;

public class ToString extends AbstractNativeFunction {
    public ToString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self instanceof Boolean) {
            return self.toString();
        }
        if (self instanceof DynBoolean) {
            return ((DynBoolean) self).getPrimitiveValue().toString();
        }

        throw new ThrowException(context, context.createTypeError("not a boolean"));
    }
}
