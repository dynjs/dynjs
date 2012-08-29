package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

public class ToExponential extends AbstractNativeFunction {
    
    public ToExponential(GlobalObject globalObject) {
        super(globalObject, "fractionDigits");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return null;
    }

}
