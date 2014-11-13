package org.dynjs.runtime.builtins.types.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class Parse extends AbstractNativeFunction {

    public Parse(GlobalContext globalContext) {
        super(globalContext, "string");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String text = Types.toString(context, args[0]);
        return parse(context, text);
    }

    public static long parse(ExecutionContext context, String text) {
        Date date = null;
        
        if (text.startsWith("T")) {
            date = attemptParse(text, "'T'HH:mm:ss.SSSZ");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "'T'HH:mm:ssZ");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "'T'HH:mmZ");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "'T'HHZ");
            if (date != null) {
                return date.getTime();
            }
        } else {
            date = attemptParse(text, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "yyyy-MM-dd");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "yyyy-MM");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "yyyy");
            if (date != null) {
                return date.getTime();
            }
            date = attemptParse(text, "EEE, dd MMM yyyy HH:mm:ss zzz");
            if (date != null) {
                return date.getTime();
            }
        }

        throw new ThrowException(context, context.createSyntaxError("unable to parse date"));
    }

    protected static Date attemptParse(String text, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return format.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/Parse.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: parse>";
    }
}
