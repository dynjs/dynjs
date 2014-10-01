package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;
import java.util.TimeZone;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;

public class ToUTCString extends DateTimeFormatter {

    public ToUTCString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.setTimeInMillis(t);
        return String.format(context.getLocale(), "%1$ta, %1$td %1$tb %1$tY %1$tH:%1$tM:%1$tS GMT", c);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/ToUTCString.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: toUTCString>";
    }
}
