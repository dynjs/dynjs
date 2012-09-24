package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.dynjs.runtime.builtins.types.date.prototype.DateTimeFormatter;
import org.dynjs.runtime.builtins.types.date.prototype.Now;
import org.dynjs.runtime.builtins.types.date.prototype.Parse;
import org.dynjs.runtime.builtins.types.date.prototype.ToDateString;
import org.dynjs.runtime.builtins.types.date.prototype.ToISOString;
import org.dynjs.runtime.builtins.types.date.prototype.ToLocaleString;
import org.dynjs.runtime.builtins.types.date.prototype.ToString;
import org.dynjs.runtime.builtins.types.date.prototype.ToTimeString;
import org.dynjs.runtime.builtins.types.date.prototype.ValueOf;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class BuiltinDate extends AbstractBuiltinType {

    public BuiltinDate(final GlobalObject globalObject) {
        super(globalObject, "year", "month", "date", "hours", "minutes", "seconds", "ms");
        final DynDate proto = new DynDate(globalObject);
        setPrototypeProperty(proto);
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynDate(context.getGlobalObject());
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        put(null, "now", new Now(globalObject), false);
        put(null, "parse", new Parse(globalObject), false);
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject));
        defineNonEnumerableProperty(proto, "toDateString", new ToDateString(globalObject));
        defineNonEnumerableProperty(proto, "toTimeString", new ToTimeString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleDateString", new DateTimeFormatter(globalObject, "MM/dd/YYYY"));
        defineNonEnumerableProperty(proto, "toLocaleTimeString", new DateTimeFormatter(globalObject, "HH:mm:ss"));
        defineNonEnumerableProperty(proto, "toISOString", new ToISOString(globalObject));
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalObject));
        defineNonEnumerableProperty(proto, "getTime", new ValueOf(globalObject));
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        DynDate date = null;
        if (self == Types.UNDEFINED) {
            date = new DynDate(context.getGlobalObject());
        } else {
            date = (DynDate) self;
        }
        if (args[0] == Types.UNDEFINED) { // 15.9.3.3
            ((DynDate) date).setPrimitiveValue(UTCnow());
        }
        return date;
    }

    private long UTCnow() {
        return DateTime.now(DateTimeZone.UTC).getMillis();
    }
}
