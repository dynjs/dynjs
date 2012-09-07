package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class Parse extends AbstractNativeFunction {

    public Parse(GlobalObject globalObject) {
        super(globalObject, "string");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        final long UTCmillis = DateTime.parse(args[0].toString(), ISODateTimeFormat.dateTime()).toDateTime(DateTimeZone.UTC).getMillis();
        return Types.toNumber(context, UTCmillis);
    }
}
