package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.BuiltinNumber;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class ToLocaleString extends AbstractNativeFunction {
    
    public ToLocaleString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self instanceof DynNumber) {
            return ((DynNumber)self).getPrimitiveValue().toString();
        }
        return "0";
    }
}