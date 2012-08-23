package org.dynjs.runtime.builtins.types.number;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class ToFixed extends AbstractNativeFunction {
    
    public ToFixed(GlobalObject globalObject) { 
        super(globalObject);
        PropertyDescriptor length = PropertyDescriptor.newAccessorPropertyDescriptor(true);
        length.set("Value", 1);
        this.defineOwnProperty(null, "length", length, false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.5
        int digits = 0;
        if (args.length == 1) {
            digits = Types.toInt32(args[0]);
        }
        if (digits < 0 || digits > 20) {
            throw new ThrowException( context.createRangeError("toFixed() digits argument must be between 0 and 20") );
        }
        if (self instanceof NaN) {
            return "NaN";
        }
        Object primitive = ((PrimitiveDynObject)self).getPrimitiveValue();
        return Double.toString((double) primitive);
    }

}
