package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.builtins.types.string.DynString;

public class ValueOf extends AbstractNativeFunction {

    public ValueOf(GlobalObject globalObject) {
        super(globalObject);
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
        throw new ThrowException(context.createTypeError("String.valueOf() only allowed on strings"));

    }

}
