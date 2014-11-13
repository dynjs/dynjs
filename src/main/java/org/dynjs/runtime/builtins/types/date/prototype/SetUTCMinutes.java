package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetUTCMinutes extends AbstractDateFunction {

    public SetUTCMinutes(GlobalContext globalContext) {
        super(globalContext, "min", "sec", "ms");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("setUTCMinutes(...) may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        long t = dateObj.getTimeValue();
        Number m = Types.toNumber(context, args[0]);

        Number s = null;

        if (args[1] != Types.UNDEFINED) {
            s = Types.toNumber(context, args[1]);
        } else {
            s = secFromTime(t);
        }

        Number millis = null;
        if (args[2] != Types.UNDEFINED) {
            millis = Types.toNumber(context, args[2]);
        } else {
            millis = msFromTime(t);
        }

        Number date = makeDate(context, day(t), makeTime(context, hourFromTime(t), m, s, millis));

        Number u = timeClip(context, date);

        dateObj.setPrimitiveValue(u);

        return u;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetUTCMinutes.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setUTCMinutes>";
    }
}
