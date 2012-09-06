package org.dynjs.runtime.builtins.types.number.prototype;

import java.math.BigDecimal;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

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
        int digits = Types.toInteger(context, args[0]);
        if (digits < 0 || digits > 20) {
            throw new ThrowException(context.createRangeError("Number.prototype.toFixed() digits argument must be between 0 and 20"));
        }
        final Number number = Types.toNumber(context, self);
        if (Double.isInfinite(number.doubleValue()) || Double.isNaN(number.doubleValue()))
            return String.valueOf(number);
        else if (number.doubleValue() < 1.0E21) {
            final BigDecimal bigDecimal = new BigDecimal(number.doubleValue());
            return bigDecimal.setScale(digits, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return rewritePossiblyExponentialValue(String.valueOf(number.doubleValue()));
        }
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
