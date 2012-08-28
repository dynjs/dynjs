package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

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
        String value = "";
        if (args.length > 1) {
            return Types.UNDEFINED;
        }
        else {
            Integer digits = 0;
            if (args.length == 1) {
                digits = (Integer) Types.toNumber(args[0]);
            }
            if (digits < 0 || digits > 20) {
                throw new ThrowException( context.createRangeError("toFixed() digits argument must be between 0 and 20") );
            }
            
            Object primitiveValue = Double.NaN;
            if (self instanceof PrimitiveDynObject) {
                primitiveValue = ((PrimitiveDynObject)self).getPrimitiveValue();
            } else if (self instanceof DynNumber) {
                primitiveValue = ((DynNumber)self).getPrimitiveValue();
            }
            if (primitiveValue instanceof Double) {
                value = doubleToFixed((double) primitiveValue, digits);
            } else {
                value = Integer.toString((int) primitiveValue);
            }
        }
        return value;
    }

    private String doubleToFixed(double _double_, int digits) {
        String value;
        if (Double.isNaN(_double_)) {
            value = "NaN";
        } else if (Double.isInfinite(_double_)) {
            if (_double_ > 0) {
                value = "Infinity";
            } else {
                value = "-Infinity";
            }
        }
        else if (_double_ >= 1.0E21) {
            value = Types.toString(_double_);
        } else {
            if (digits == 0) {
                value = String.valueOf(Math.round(_double_));
            } else {
                value = Double.toString(_double_);
            }
        }
        // TODO: We have to make exponential values look like JS and not Java
        return value;
    }
}
