package org.dynjs.runtime.builtins.types.date.prototype;

import java.util.Calendar;
import java.util.TimeZone;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class ToISOString extends DateTimeFormatter {

    public ToISOString(GlobalContext globalContext) {
        super(globalContext);
    }
    
    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof DynDate)) {
            throw new ThrowException(context, context.createTypeError("toISOString() may only be used with Dates"));
        }

        DynDate dateObj = (DynDate) self;

        if (dateObj.isNaN() ) {
            throw new ThrowException( context, context.createRangeError( "date is out of range" ));
        }
        
        long t = dateObj.getTimeValue();
        
        if ( t > 8640000000000000L || t <  -8640000000000000L) {
            throw new ThrowException( context, context.createRangeError( "date is out of range" ));
        }
        
        return format(context, dateObj.getTimeValue());
    }

    @Override
    public String format(ExecutionContext context, long t) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone( "GMT" ));
        c.setTimeInMillis(t);
        return String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tLZ", c);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/ToISOString.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: toISOString>";
    }
}
