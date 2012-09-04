package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

public class IsNaN extends AbstractNativeFunction {

    public IsNaN(GlobalObject globalObject) {
        super(globalObject, "text");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if (o != Types.UNDEFINED) {
            return isNaN(context, args);
        } else {
            return Types.UNDEFINED;
        }
    }

    static boolean isNaN(ExecutionContext context, Object... args) {
        Object arg = args[0];
        if (isNullOrBooleanOrWhiteSpace(arg)) {
            return false;
        } else if (arg instanceof Reference) {
            Object value = ((Reference) arg).getValue(context);
            return value.equals(Double.POSITIVE_INFINITY) || value.equals(Double.NEGATIVE_INFINITY);
        }
        int radix = ParseInt.extractRadix(Types.toString(context, arg));
        String text = ParseInt.cleanText(Types.toString(context, arg), radix);
        return (ParseInt.parseInt(text, radix).equals(Double.NaN));
    }

    static boolean isNullOrBooleanOrWhiteSpace(Object arg) {
        if (arg == Types.NULL || arg instanceof Boolean) {
            return true;
        } else if (arg instanceof String) {
            String value = ((String) arg).trim();
            return value.equals("");
        }
        return false;
    }

}
