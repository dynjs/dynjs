package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;

public class ToLocaleTimeString extends DateTimeFormatter {
    public ToLocaleTimeString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(context.getTimeZone());
        c.setTimeInMillis(t);
        return String.format("%1$tH:%1$tM:%1$tS", c);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/ToLocaleTimeString.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: toLocaleTimeString>";
    }
}
