package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

public class ToTimeString extends DateTimeFormatter {
    public ToTimeString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(context.getTimeZone());
        c.setTimeInMillis(t);
        return String.format("%1$tH:%1$tM:%1$tS GMT%1$tz (%1$tZ)", c);
    }
}
