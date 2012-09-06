package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        PrimitiveDynObject str = (PrimitiveDynObject) self;
        if (!str.getClassName().equals("String")) {
            throw new ThrowException(context.createTypeError("String.toString() only allowed on strings"));
        }

        return str.getPrimitiveValue();
    }

}
