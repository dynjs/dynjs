package org.dynjs.runtime.builtins.types.date;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class UTC extends AbstractDateFunction {

    public UTC(GlobalObject globalObject) {
        super(globalObject, "year", "month", "date", "hours", "minutes", "seconds", "ms" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Number y = Types.toNumber(context, args[0]);
        Number m = Types.toNumber(context, args[1]);
        Number dt = 1;
        Number h = 0;
        Number min = 0;
        Number s = 0;
        Number milli = 0;
        
        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");
        
        if (numArgs >= 3) {
            dt = Types.toNumber(context, args[2]);
        }
        if (numArgs >= 4) {
            h = Types.toNumber(context, args[3]);
        }
        if (numArgs >= 5) {
            min = Types.toNumber(context, args[4]);
        }
        if (numArgs >= 6) {
            s = Types.toNumber(context, args[5]);
        }
        if (numArgs >= 7) {
            milli = Types.toNumber(context, args[6]);
        }

        Number yr = y;

        if (!Double.isNaN(y.doubleValue())) {
            long longYr = yr.longValue();
            yr = Types.toInteger(context, y);
            if (longYr >= 0 && longYr <= 99) {
                yr = longYr + 1900;
            }
        }

        Number finalDate = makeDate(context, makeDay(context, yr, m, dt), makeTime(context, h, min, s, milli));
        Number clipped = timeClip(context, finalDate);

        return clipped;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/UTC.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: utc>";
    }
}
