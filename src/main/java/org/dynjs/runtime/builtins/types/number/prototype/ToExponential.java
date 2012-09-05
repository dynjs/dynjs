package org.dynjs.runtime.builtins.types.number.prototype;

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
        Number x = Types.toNumber(context, self);
        Integer f = Types.toInteger(context, args[0]);
        double xd = x.doubleValue();
        int xi = x.intValue();

        if (Double.isNaN(xd)) {
            return "NaN";
        }

        String s = "";
        String m = "0";
        String c = "+";
        String d = "0";

        if (xi < 0) {
            s = "-";
            xd = -xd;
            xi = -xi;
        }

        if (Double.isInfinite(xd)) {
            return s + "Infinity";
        }
        if (xi != 0) {
            // TODO: steps 9-12 
        }

        if (f < 0 || f > 20) {
            throw new ThrowException(context.createRangeError("toExponential() digits argument must be between 0 and 20"));
        }
        return s + m + "e" + c + d;
    }
}
