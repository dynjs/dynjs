package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;
import java.util.TimeZone;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

public class ToUTCString extends DateTimeFormatter {

    public ToUTCString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.setTimeInMillis(t);
        return String.format(context.getLocale(), "%1$ta, %1$td %1$tb %1$tY %1$tH:%1$tM:%1$tS GMT",  c );
    }
}
