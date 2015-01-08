package org.dynjs.runtime.builtins.types.number.prototype;

import java.math.BigDecimal;
import java.math.MathContext;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class ToPrecision extends AbstractNativeFunction {

    public ToPrecision(GlobalContext globalContext) {
        super(globalContext, "precision");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.7 Number.prototype.toPrecision (precision)
        if (args[0] == Types.UNDEFINED) {
            return Types.toString(context, self);
        }
        Number number  = Types.toNumber(context, self);
        Long precision = Types.toInteger(context, args[0]);
        if (Double.isNaN(number.doubleValue()) || Double.isInfinite(number.doubleValue())) {
            return String.valueOf(number);
        }
        if (precision < 1 || precision > 21) {
            throw new ThrowException(context, context.createRangeError("Number.prototype.toPrecision() [precision] must be between 0 and 20"));
        }
        return new BigDecimal(number.doubleValue(), new MathContext(precision.intValue())).toString();
    }
}
