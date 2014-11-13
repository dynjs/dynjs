package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetSeconds extends AbstractDateFunction {

    public SetSeconds(GlobalContext globalContext) {
        super(globalContext, "sec", "ms");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("setSeconds(...) may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        long t = localTime(context, dateObj.getTimeValue());
        Number s = Types.toNumber(context, args[0]);

        Number millis = null;
        if (args[1] != Types.UNDEFINED) {
            millis = Types.toNumber(context, args[1]);
        } else {
            millis = msFromTime(t);
        }

        Number date = makeDate(context, day(t), makeTime(context, hourFromTime(t), minFromTime(t), s, millis));

        Number u = timeClip(context, utc(context, date));

        dateObj.setPrimitiveValue(u);

        return u;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetSeconds.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setSeconds>";
    }
}
