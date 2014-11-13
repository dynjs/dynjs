package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.string.DynString;

public class ValueOf extends AbstractNativeFunction {

    public ValueOf(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.4
        if (self instanceof String) {
            return self;
        }
        if (self instanceof DynString) {
            return ((DynString) self).getPrimitiveValue();
        }
        throw new ThrowException(context, context.createTypeError("String.valueOf() only allowed on strings"));

    }

}
