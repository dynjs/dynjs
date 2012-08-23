package org.dynjs.runtime.builtins.types.number;

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
        if (self instanceof DynNumber) {
            return ((DynNumber)self).getPrimitiveValue();
        }
        if (BuiltinNumber.isNumber((DynObject) self)) {
            return ((PrimitiveDynObject)self).getPrimitiveValue();
        }
        return "TypeError";
    }
}