package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class ToString extends AbstractNativeFunction {
    public ToString(GlobalObject globalObject) {
        super(globalObject, "[radix]");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        long radix = Types.toInteger(context, args[0]);
        if (args[0] == Types.UNDEFINED) {
            radix = 10;
        }
        if (radix < 2 || radix > 36 ) {
            throw new ThrowException(context, context.createRangeError("Number.prototype.toString([radix]) must have a radix between 2 and 36, inclusive."));
        }

        if (self instanceof DynNumber || self instanceof Long || self instanceof Double || self instanceof Integer) {
            if (radix == 10) {
                return Types.toString(context, Types.toNumber(context, self));
            } else {
                return Types.toNumber(context, self).toString();
            }
        }
        throw new ThrowException(context, context.createTypeError("Number.prototype.toString() only allowed on Numbers"));
    }
}
