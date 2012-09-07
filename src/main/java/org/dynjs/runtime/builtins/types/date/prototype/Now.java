package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

public class Now extends AbstractNativeFunction {

    public Now(GlobalObject globalObject, String... formalParameters) {
        super(globalObject, formalParameters);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        final long valueOfUTCmillis = Instant.now().toDateTime(DateTimeZone.UTC).getMillis();
        return Types.toNumber(context, valueOfUTCmillis);
    }
}
