package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.builtins.types.BuiltinNumber;

public class ToLocaleString extends AbstractNativeFunction {
    
    public ToLocaleString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (BuiltinNumber.isNumber((DynObject) self)) {
            return ((PrimitiveDynObject)self).getPrimitiveValue().toString();
        }
        return "0";
    }
}