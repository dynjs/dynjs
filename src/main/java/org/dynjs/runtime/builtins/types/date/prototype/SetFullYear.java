package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetFullYear extends AbstractDateFunction {

    public SetFullYear(GlobalContext globalContext) {
        super(globalContext, "year", "month", "date");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {

        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("setFullYear(...) may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        long t = localTime(context, dateObj.getTimeValue());

        Number y = Types.toNumber(context, args[0]);

        Number m = null;

        if (args[1] != Types.UNDEFINED) {
            m = Types.toNumber(context, args[1]);
        } else {
            m = monthFromTime(t);
        }

        Number dt = null;

        if (args[1] != Types.UNDEFINED) {
            dt = Types.toNumber(context, args[2]);
        } else {
            dt = dateFromTime(t);
        }

        Number newDate = makeDate(context, makeDay(context, y, m, dt), timeWithinDay(t));

        Number u = timeClip(context, utc(context, newDate));

        dateObj.setTimeValue(u);

        return u;

    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetFullYear.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setFullYear>";
    }
}
