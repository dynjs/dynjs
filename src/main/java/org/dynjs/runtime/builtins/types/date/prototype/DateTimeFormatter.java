package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public abstract class DateTimeFormatter extends AbstractDateFunction {

    public DateTimeFormatter(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("getMonth() may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        if (dateObj.isNaN()) {
            return "NaN";
        }

        return format(context, dateObj.getTimeValue());
    }

    public abstract String format(ExecutionContext context, long t);
}
