package org.dynjs.runtime.builtins.types.number.prototype;

import java.math.BigDecimal;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class ToFixed extends AbstractNativeFunction {

    public ToFixed(GlobalContext globalContext) {
        super(globalContext, "fractionDigits");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.5
        if (args.length > 1) {
            return Types.UNDEFINED;
        }
        long digits = Types.toInteger(context, args[0]);
        if (digits < 0L || digits > 20L) {
            throw new ThrowException(context, context.createRangeError("Number.prototype.toFixed() digits argument must be between 0 and 20"));
        }
        final Number number = Types.toNumber(context, self);
        if (Double.isInfinite(number.doubleValue()) || Double.isNaN(number.doubleValue()))
            return String.valueOf(number);
        else if (number.doubleValue() < 1.0E21) {
            final BigDecimal bigDecimal = new BigDecimal(number.doubleValue());
            return bigDecimal.setScale((int)digits, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return Types.rewritePossiblyExponentialValue(String.valueOf(number.doubleValue()));
        }
    }
}
