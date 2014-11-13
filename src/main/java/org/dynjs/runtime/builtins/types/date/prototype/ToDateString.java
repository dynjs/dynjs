package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;

public class ToDateString extends DateTimeFormatter {

    public ToDateString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(context.getTimeZone());
        c.setTimeInMillis(t);
        return String.format(context.getLocale(), "%1$ta %1$tb %1$td %1$tY", c);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/ToDateString.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: toDateString>";
    }

}
