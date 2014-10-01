package org.dynjs.runtime.builtins.types.number.prototype;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class ToExponential extends AbstractNativeFunction {

    public ToExponential(GlobalContext globalContext) {
        super(globalContext, "fractionDigits");
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
            throw new ThrowException(context, context.createRangeError("Number.prototype.toExponential() [fractionDigits] must be between 0 and 20"));
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00##################E0");
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        decimalFormat.setMinimumFractionDigits(fractionDigits.intValue());
        return Types.rewritePossiblyExponentialValue(decimalFormat.format(number.doubleValue()));
    }
}
