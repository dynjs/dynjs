package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class GetTimezoneOffset extends AbstractDateFunction {

    public GetTimezoneOffset(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("getMinutes() may only be used with Dates"));
        }

        DynDate date = (DynDate) self;

        if (date.isNaN()) {
            return Double.NaN;
        }

        long t = date.getTimeValue();
        return (long) (t - localTime(context, t)) / MS_PER_MINUTE;
    }
}
