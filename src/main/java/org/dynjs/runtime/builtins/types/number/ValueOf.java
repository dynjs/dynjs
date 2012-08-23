package org.dynjs.runtime.builtins.types.number;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.builtins.types.BuiltinNumber;

public class ValueOf extends AbstractNativeFunction {
    public ValueOf(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.4
        if (BuiltinNumber.isNumber((DynObject) self)) {
            if (self instanceof DynNumber) {
                return ((DynNumber)self).getPrimitiveValue();
            }
            return ((PrimitiveDynObject)self).getPrimitiveValue();
        }
        throw new ThrowException( context.createTypeError( "Number.valueOf() only allowed on Numbers" ));
    }
}