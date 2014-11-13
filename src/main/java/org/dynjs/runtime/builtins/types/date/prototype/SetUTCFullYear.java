package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetUTCFullYear extends AbstractDateFunction {

    public SetUTCFullYear(GlobalContext globalContext) {
        super(globalContext, "year", "month", "date");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("setUTCFullYear(...) may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        long t = dateObj.getTimeValue();

        Number y = Types.toNumber(context, args[0]);
        
        Number m = null;
        
        if ( args[1] != Types.UNDEFINED ) {
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

        Number newDate = makeDate(context, makeDay(context, yearFromTime(t), m, dt), timeWithinDay(t));

        Number u = timeClip(context, newDate);

        dateObj.setTimeValue(u);

        return u;

    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetUTCFullYear.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setUTCFullYear>";
    }
}
