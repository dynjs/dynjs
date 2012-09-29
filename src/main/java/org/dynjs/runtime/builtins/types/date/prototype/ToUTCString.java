package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class ToUTCString extends AbstractNativeFunction {

    public ToUTCString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, dd MMM YYYY HH:mm:ss 'GMT'").withZoneUTC();
        return new DateTime((Long) ((DynDate) self).getPrimitiveValue()).toString(formatter);
    }

}
