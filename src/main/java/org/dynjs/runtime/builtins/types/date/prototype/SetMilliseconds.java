package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class SetMilliseconds extends AbstractDateFunction {

    public SetMilliseconds(GlobalContext globalContext) {
        super(globalContext, "ms" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("setMilliseconds(...) may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        long t = localTime(context, dateObj.getTimeValue());

        Number time = makeTime(context, hourFromTime(t), minFromTime(t), secFromTime(t), Types.toNumber(context, args[0]));
        Number u = timeClip(context, utc(context, makeDate(context, day(t), time)));

        dateObj.setTimeValue(u);

        return u;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/SetMilliseconds.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: setMilliseconds>";
    }
}
