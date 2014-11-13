package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.date.AbstractDateFunction;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class GetFullYear extends AbstractDateFunction {

    public GetFullYear(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("getDay() may only be used with Dates"));
        }

        DynDate date = (DynDate) self;

        if (date.isNaN()) {
            return Double.NaN;
        }

        long t = date.getTimeValue();
        return yearFromTime(localTime(context, t));
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/GetFullYear.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: getFullYear>";
    }
}
