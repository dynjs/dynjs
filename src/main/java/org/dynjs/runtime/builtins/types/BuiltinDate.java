package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
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
        defineNonEnumerableProperty(proto, "getFullYear", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime(self).year().get();
            }
        });
        defineNonEnumerableProperty(proto, "getMonth", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime(self).monthOfYear().get();
            }
        });
        defineNonEnumerableProperty(proto, "getDay", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime(self).dayOfWeek().get();
            }
        });
        defineNonEnumerableProperty(proto, "getHours", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).hourOfDay().get();
            }
        });
        defineNonEnumerableProperty(proto, "getMinutes", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).minuteOfHour().get();
            }
        });
        defineNonEnumerableProperty(proto, "getSeconds", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).secondOfMinute().get();
            }
        });
        defineNonEnumerableProperty(proto, "getUTCFullYear", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime(self).toDateTime(DateTimeZone.UTC).year().get();
            }
        });
        defineNonEnumerableProperty(proto, "getUTCMonth", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime(self).toDateTime(DateTimeZone.UTC).monthOfYear().get();
            }
        });
        defineNonEnumerableProperty(proto, "getUTCDay", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime(self).toDateTime(DateTimeZone.UTC).dayOfWeek().get();
            }
        });
        defineNonEnumerableProperty(proto, "getUTCHours", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).toDateTime(DateTimeZone.UTC).hourOfDay().get();
            }
        });
        defineNonEnumerableProperty(proto, "getUTCMinutes", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).toDateTime(DateTimeZone.UTC).minuteOfHour().get();
            }
        });
        defineNonEnumerableProperty(proto, "getUTCSeconds", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).toDateTime(DateTimeZone.UTC).secondOfMinute().get();
            }
        });
        defineNonEnumerableProperty(proto, "getTimezoneOffset", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).toDateTime().getZone().toTimeZone().getRawOffset();
            }
        });
        defineNonEnumerableProperty(proto, "getMilliseconds", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).toDateTime().getMillisOfSecond();
            }
        });
        defineNonEnumerableProperty(proto, "setMilliseconds", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return getDateTime((DynDate) self).toDateTime(DateTimeZone.UTC).getMillis();
            }
        });
    }

    private DateTime getDateTime(Object self) {
        return new DateTime((Long) ((DynDate) self).getPrimitiveValue());
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        DynDate date = null;
        if (self == Types.UNDEFINED || self == Types.NULL) {
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
