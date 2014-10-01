package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetHours extends AbstractDateFunction {

    public SetHours(GlobalContext globalContext) {
        super(globalContext, "hour", "min", "sec", "ms");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("setHours(...) may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        long t = localTime(context, dateObj.getTimeValue());

        Number h = Types.toNumber(context, args[0]);

        Number m = null;
        if (args[1] != Types.UNDEFINED) {
            m = Types.toNumber(context, args[1]);
        } else {
            m = minFromTime(t);
        }

        Number s = null;

        if (args[2] != Types.UNDEFINED) {
            s = Types.toNumber(context, args[2]);
        } else {
            s = secFromTime(t);
        }

        Number millis = null;
        if (args[3] != Types.UNDEFINED) {
            millis = Types.toNumber(context, args[3]);
        } else {
            millis = msFromTime(t);
        }

        Number date = makeDate(context, day(t), makeTime(context, h, m, s, millis));

        Number u = timeClip(context, utc(context, date));

        dateObj.setPrimitiveValue(u);

        return u;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetHours.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setHours>";
    }
}
