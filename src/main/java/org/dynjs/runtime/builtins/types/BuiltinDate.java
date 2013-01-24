package org.dynjs.runtime.builtins.types;

import static org.dynjs.runtime.builtins.types.date.AbstractDateFunction.*;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.date.DynDate;
import org.dynjs.runtime.builtins.types.date.Parse;
import org.dynjs.runtime.builtins.types.date.UTC;
import org.dynjs.runtime.builtins.types.date.prototype.GetDate;
import org.dynjs.runtime.builtins.types.date.prototype.GetDay;
import org.dynjs.runtime.builtins.types.date.prototype.GetFullYear;
import org.dynjs.runtime.builtins.types.date.prototype.GetHours;
import org.dynjs.runtime.builtins.types.date.prototype.GetMilliseconds;
import org.dynjs.runtime.builtins.types.date.prototype.GetMinutes;
import org.dynjs.runtime.builtins.types.date.prototype.GetMonth;
import org.dynjs.runtime.builtins.types.date.prototype.GetSeconds;
import org.dynjs.runtime.builtins.types.date.prototype.GetTimezoneOffset;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCDate;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCDay;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCFullYear;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCHours;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCMilliseconds;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCMinutes;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCMonth;
import org.dynjs.runtime.builtins.types.date.prototype.GetUTCSeconds;
import org.dynjs.runtime.builtins.types.date.prototype.GetYear;
import org.dynjs.runtime.builtins.types.date.prototype.Now;
import org.dynjs.runtime.builtins.types.date.prototype.SetDate;
import org.dynjs.runtime.builtins.types.date.prototype.SetFullYear;
import org.dynjs.runtime.builtins.types.date.prototype.SetHours;
import org.dynjs.runtime.builtins.types.date.prototype.SetMilliseconds;
import org.dynjs.runtime.builtins.types.date.prototype.SetMinutes;
import org.dynjs.runtime.builtins.types.date.prototype.SetMonth;
import org.dynjs.runtime.builtins.types.date.prototype.SetSeconds;
import org.dynjs.runtime.builtins.types.date.prototype.SetTime;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCDate;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCFullYear;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCHours;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCMilliseconds;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCMinutes;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCMonth;
import org.dynjs.runtime.builtins.types.date.prototype.SetUTCSeconds;
import org.dynjs.runtime.builtins.types.date.prototype.SetYear;
import org.dynjs.runtime.builtins.types.date.prototype.ToDateString;
import org.dynjs.runtime.builtins.types.date.prototype.ToISOString;
import org.dynjs.runtime.builtins.types.date.prototype.ToJSON;
import org.dynjs.runtime.builtins.types.date.prototype.ToLocaleDateString;
import org.dynjs.runtime.builtins.types.date.prototype.ToLocaleString;
import org.dynjs.runtime.builtins.types.date.prototype.ToLocaleTimeString;
import org.dynjs.runtime.builtins.types.date.prototype.ToString;
import org.dynjs.runtime.builtins.types.date.prototype.ToTimeString;
import org.dynjs.runtime.builtins.types.date.prototype.ToUTCString;
import org.dynjs.runtime.builtins.types.date.prototype.ValueOf;

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
        proto.setPrototype(globalObject.getPrototypeFor("Object"));

        defineNonEnumerableProperty(this, "now", new Now(globalObject) );
        defineNonEnumerableProperty(this, "parse", new Parse(globalObject));
        defineNonEnumerableProperty(this, "UTC", new UTC(globalObject) );
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject));
        defineNonEnumerableProperty(proto, "toDateString", new ToDateString(globalObject));
        defineNonEnumerableProperty(proto, "toTimeString", new ToTimeString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleDateString", new ToLocaleDateString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleTimeString", new ToLocaleTimeString(globalObject));
        defineNonEnumerableProperty(proto, "toISOString", new ToISOString(globalObject));
        defineNonEnumerableProperty(proto, "toUTCString", new ToUTCString(globalObject));
        defineNonEnumerableProperty(proto, "toGMTString", new ToUTCString(globalObject));
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalObject));
        defineNonEnumerableProperty(proto, "getTime", new ValueOf(globalObject));
        defineNonEnumerableProperty(proto, "getFullYear", new GetFullYear(globalObject));
        defineNonEnumerableProperty(proto, "getMonth", new GetMonth(globalObject));
        defineNonEnumerableProperty(proto, "getDay", new GetDay(globalObject));
        defineNonEnumerableProperty(proto, "getHours", new GetHours(globalObject));
        defineNonEnumerableProperty(proto, "getMinutes", new GetMinutes(globalObject));
        defineNonEnumerableProperty(proto, "getSeconds", new GetSeconds(globalObject));
        defineNonEnumerableProperty(proto, "getUTCFullYear", new GetUTCFullYear(globalObject));
        defineNonEnumerableProperty(proto, "getUTCMonth", new GetUTCMonth(globalObject));
        defineNonEnumerableProperty(proto, "getUTCDay", new GetUTCDay(globalObject));
        defineNonEnumerableProperty(proto, "getUTCHours", new GetUTCHours(globalObject));
        defineNonEnumerableProperty(proto, "getUTCMinutes", new GetUTCMinutes(globalObject));
        defineNonEnumerableProperty(proto, "getUTCSeconds", new GetUTCSeconds(globalObject));
        defineNonEnumerableProperty(proto, "getTimezoneOffset", new GetTimezoneOffset(globalObject));
        defineNonEnumerableProperty(proto, "getMilliseconds", new GetMilliseconds(globalObject));
        defineNonEnumerableProperty(proto, "setMilliseconds", new SetMilliseconds(globalObject));
        defineNonEnumerableProperty(proto, "getUTCMilliseconds", new GetUTCMilliseconds(globalObject));
        defineNonEnumerableProperty(proto, "setUTCMilliseconds", new SetUTCMilliseconds(globalObject));
        defineNonEnumerableProperty(proto, "setTime", new SetTime(globalObject));
        defineNonEnumerableProperty(proto, "setSeconds", new SetSeconds(globalObject));
        defineNonEnumerableProperty(proto, "setUTCSeconds", new SetUTCSeconds(globalObject));
        defineNonEnumerableProperty(proto, "setMinutes", new SetMinutes(globalObject));
        defineNonEnumerableProperty(proto, "setUTCMinutes", new SetUTCMinutes(globalObject));
        defineNonEnumerableProperty(proto, "setHours", new SetHours(globalObject));
        defineNonEnumerableProperty(proto, "setUTCHours", new SetUTCHours(globalObject));
        defineNonEnumerableProperty(proto, "setDate", new SetDate(globalObject));
        defineNonEnumerableProperty(proto, "getDate", new GetDate(globalObject));
        defineNonEnumerableProperty(proto, "setUTCDate", new SetUTCDate(globalObject));
        defineNonEnumerableProperty(proto, "getUTCDate", new GetUTCDate(globalObject));
        defineNonEnumerableProperty(proto, "setMonth", new SetMonth(globalObject));
        defineNonEnumerableProperty(proto, "setUTCMonth", new SetUTCMonth(globalObject));
        defineNonEnumerableProperty(proto, "setFullYear", new SetFullYear(globalObject));
        defineNonEnumerableProperty(proto, "setUTCFullYear", new SetUTCFullYear(globalObject));
        defineNonEnumerableProperty(proto, "toJSON", new ToJSON(globalObject));

        defineNonEnumerableProperty(proto, "getYear", new GetYear(globalObject));
        defineNonEnumerableProperty(proto, "setYear", new SetYear(globalObject));
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self == Types.UNDEFINED || self == Types.NULL) {
            DynDate now = new DynDate(context.getGlobalObject());
            JSFunction toString = (JSFunction) now.get(context, "toString");
            return context.call(toString, now);
        }

        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");

        DynDate date = (DynDate) self;

        if (numArgs == 0) {
            date.setTimeValue(context.getClock().currentTimeMillis());
        } else if (numArgs == 1) {
            Object v = Types.toPrimitive(context, args[0]);
            if (v instanceof String) {
                date.setTimeValue(timeClip(context, Parse.parse(context, (String) v)));
            } else {
                date.setTimeValue(timeClip(context, Types.toNumber(context, v)));
            }
        } else {
            Number y = Types.toNumber(context, args[0]);
            Number m = Types.toNumber(context, args[1]);
            Number dt = 1;
            Number h = 0;
            Number min = 0;
            Number s = 0;
            Number milli = 0;
            if (numArgs >= 3) {
                dt = Types.toNumber(context, args[2]);
            }
            if (numArgs >= 4) {
                h = Types.toNumber(context, args[3]);
            }
            if (numArgs >= 5) {
                min = Types.toNumber(context, args[4]);
            }
            if (numArgs >= 6) {
                s = Types.toNumber(context, args[5]);
            }
            if (numArgs >= 7) {
                milli = Types.toNumber(context, args[6]);
            }

            Number yr = y;
            
            if (!Double.isNaN(y.doubleValue())) {
                long longYr = yr.longValue();
                if (longYr >= 0 && longYr <= 99) {
                    yr = longYr + 1900;
                }
            }

            Number finalDate = makeDate(context, makeDay(context, yr, m, dt), makeTime(context, h, min, s, milli));
            Number clipped = timeClip(context, utc(context, finalDate));

            date.setTimeValue(clipped);
        }

        return date;
    }
}
