package org.dynjs.runtime.builtins.types.number.prototype;

import java.math.BigDecimal;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class ToExponential extends AbstractNativeFunction {

    public ToExponential(GlobalObject globalObject) {
        super(globalObject, "fractionDigits");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.6 Number.prototype.toExponential (fractionDigits) 
        Number number = Types.toNumber(context, self);
        Long fractionDigits = Types.toInteger(context, args[0]);

        if (Double.isNaN(number.doubleValue()) || Double.isInfinite(number.doubleValue())) {
            return String.valueOf(number);
        }
        if (fractionDigits < 0 || fractionDigits > 20) {
            throw new ThrowException(context.createRangeError("Number.prototype.toExponential() [fractionDigits] must be between 0 and 20"));
        }
        if (number.doubleValue() == 0) { return "0e+0"; }
        final BigDecimal bigDecimal = new BigDecimal(number.doubleValue());
        return Types.rewritePossiblyExponentialValue(bigDecimal.setScale(-1, BigDecimal.ROUND_HALF_UP).toString());
    }
}
