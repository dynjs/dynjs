package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.dynjs.runtime.builtins.types.date.prototype.Now;
import org.dynjs.runtime.builtins.types.date.prototype.Parse;
import org.dynjs.runtime.builtins.types.date.prototype.ToDateString;
import org.dynjs.runtime.builtins.types.date.prototype.ToISOString;
import org.dynjs.runtime.builtins.types.date.prototype.ToString;
import org.dynjs.runtime.builtins.types.date.prototype.ToTimeString;
import org.dynjs.runtime.builtins.types.date.prototype.ValueOf;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class BuiltinDate extends AbstractBuiltinType {

    public BuiltinDate(final GlobalObject globalObject) {
        super(globalObject, "year", "month", "date", "hours", "minutes", "seconds", "ms");
        final DynDate proto = new DynDate(globalObject);
        put(null, "prototype", proto, false);
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynDate(context.getGlobalObject());
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject prototype) {
        put(null, "now", new Now(globalObject), false);
        put(null, "parse", new Parse(globalObject), false);
        prototype.put(null, "constructor", this, false);
        prototype.put(null, "toString", new ToString(globalObject), false);
        prototype.put(null, "toDateString", new ToDateString(globalObject), false);
        prototype.put(null, "toTimeString", new ToTimeString(globalObject), false);
        prototype.put(null, "toISOString", new ToISOString(globalObject), false);
        prototype.put(null, "valueOf", new ValueOf(globalObject), false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (args[0] == Types.UNDEFINED) { // 15.9.3.3
            ((DynDate) self).setPrimitiveValue(UTCnow());
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private long UTCnow() {
        return DateTime.now(DateTimeZone.UTC).getMillis();
    }
}
