package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;
import java.util.TimeZone;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

public class ToISOString extends DateTimeFormatter {

    public ToISOString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone( "GMT" ));
        c.setTimeInMillis(t);
        return String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tLZ", c);
    }
}
