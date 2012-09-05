package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class ToFixed extends AbstractNativeFunction {

    public ToFixed(GlobalObject globalObject) {
        super(globalObject, "fractionDigits");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.5
        if (args.length > 1) {
            return Types.UNDEFINED;
        }
        String value = "";
        Integer digits = 0;
        if (args[0] != Types.UNDEFINED) {
            digits = (Integer) Types.toNumber(context, args[0]);
        }
        if (digits < 0 || digits > 20) {
            throw new ThrowException(context.createRangeError("toFixed() digits argument must be between 0 and 20"));
        }

        Object primitiveValue = Double.NaN;
        if (self instanceof PrimitiveDynObject) {
            primitiveValue = ((PrimitiveDynObject) self).getPrimitiveValue();
        } else if (self instanceof DynNumber) {
            primitiveValue = ((DynNumber) self).getPrimitiveValue();
        }
        if (primitiveValue instanceof Double) {
            value = doubleToFixed(context, (double) primitiveValue, digits);
        } else {
            value = Integer.toString((int) primitiveValue);
        }
        return value;
    }

    private String doubleToFixed(ExecutionContext context, double _double_, int digits) {
        String value;
        if (Double.isNaN(_double_)) {
            value = "NaN";
        } else if (Double.isInfinite(_double_)) {
            if (_double_ > 0) {
                value = "Infinity";
            } else {
                value = "-Infinity";
            }
        } else if (_double_ >= 1.0E21) {
            value = Types.toString(context, _double_);
        } else {
            if (digits == 0) {
                value = String.valueOf(new Integer((int) _double_));
            } else {
                value = Double.toString(_double_);
            }
        }
        // TODO: We have to make exponential values look like JS and not Java
        return rewritePossiblyExponentialValue(value);
    }

    private String rewritePossiblyExponentialValue(String value) {
        // Java writes exponential values as 1.0E14 while JS likes
        // them as 1e+14
        final int index = value.indexOf(".0E");
        if (index != -1) {
            value = value.substring(0, index) + "e+" + value.substring(index + 3);
        }
        return value;
    }
}
