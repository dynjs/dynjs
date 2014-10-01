package org.dynjs.runtime.builtins.types;

import static org.dynjs.runtime.builtins.types.date.AbstractDateFunction.*;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
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

    public BuiltinDate(final GlobalContext globalContext) {
        super(globalContext, "year", "month", "date", "hours", "minutes", "seconds", "ms");
        final DynDate proto = new DynDate(globalContext);
        setPrototypeProperty(proto);
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        return new DynDate(context.getGlobalContext());
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject proto) {
        proto.setPrototype(globalContext.getPrototypeFor("Object"));

        defineNonEnumerableProperty(this, "now", new Now(globalContext) );
        defineNonEnumerableProperty(this, "parse", new Parse(globalContext));
        defineNonEnumerableProperty(this, "UTC", new UTC(globalContext) );
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext));
        defineNonEnumerableProperty(proto, "toDateString", new ToDateString(globalContext));
        defineNonEnumerableProperty(proto, "toTimeString", new ToTimeString(globalContext));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalContext));
        defineNonEnumerableProperty(proto, "toLocaleDateString", new ToLocaleDateString(globalContext));
        defineNonEnumerableProperty(proto, "toLocaleTimeString", new ToLocaleTimeString(globalContext));
        defineNonEnumerableProperty(proto, "toISOString", new ToISOString(globalContext));
        defineNonEnumerableProperty(proto, "toUTCString", new ToUTCString(globalContext));
        defineNonEnumerableProperty(proto, "toGMTString", new ToUTCString(globalContext));
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalContext));
        defineNonEnumerableProperty(proto, "getTime", new ValueOf(globalContext));
        defineNonEnumerableProperty(proto, "getFullYear", new GetFullYear(globalContext));
        defineNonEnumerableProperty(proto, "getMonth", new GetMonth(globalContext));
        defineNonEnumerableProperty(proto, "getDay", new GetDay(globalContext));
        defineNonEnumerableProperty(proto, "getHours", new GetHours(globalContext));
        defineNonEnumerableProperty(proto, "getMinutes", new GetMinutes(globalContext));
        defineNonEnumerableProperty(proto, "getSeconds", new GetSeconds(globalContext));
        defineNonEnumerableProperty(proto, "getUTCFullYear", new GetUTCFullYear(globalContext));
        defineNonEnumerableProperty(proto, "getUTCMonth", new GetUTCMonth(globalContext));
        defineNonEnumerableProperty(proto, "getUTCDay", new GetUTCDay(globalContext));
        defineNonEnumerableProperty(proto, "getUTCHours", new GetUTCHours(globalContext));
        defineNonEnumerableProperty(proto, "getUTCMinutes", new GetUTCMinutes(globalContext));
        defineNonEnumerableProperty(proto, "getUTCSeconds", new GetUTCSeconds(globalContext));
        defineNonEnumerableProperty(proto, "getTimezoneOffset", new GetTimezoneOffset(globalContext));
        defineNonEnumerableProperty(proto, "getMilliseconds", new GetMilliseconds(globalContext));
        defineNonEnumerableProperty(proto, "setMilliseconds", new SetMilliseconds(globalContext));
        defineNonEnumerableProperty(proto, "getUTCMilliseconds", new GetUTCMilliseconds(globalContext));
        defineNonEnumerableProperty(proto, "setUTCMilliseconds", new SetUTCMilliseconds(globalContext));
        defineNonEnumerableProperty(proto, "setTime", new SetTime(globalContext));
        defineNonEnumerableProperty(proto, "setSeconds", new SetSeconds(globalContext));
        defineNonEnumerableProperty(proto, "setUTCSeconds", new SetUTCSeconds(globalContext));
        defineNonEnumerableProperty(proto, "setMinutes", new SetMinutes(globalContext));
        defineNonEnumerableProperty(proto, "setUTCMinutes", new SetUTCMinutes(globalContext));
        defineNonEnumerableProperty(proto, "setHours", new SetHours(globalContext));
        defineNonEnumerableProperty(proto, "setUTCHours", new SetUTCHours(globalContext));
        defineNonEnumerableProperty(proto, "setDate", new SetDate(globalContext));
        defineNonEnumerableProperty(proto, "getDate", new GetDate(globalContext));
        defineNonEnumerableProperty(proto, "setUTCDate", new SetUTCDate(globalContext));
        defineNonEnumerableProperty(proto, "getUTCDate", new GetUTCDate(globalContext));
        defineNonEnumerableProperty(proto, "setMonth", new SetMonth(globalContext));
        defineNonEnumerableProperty(proto, "setUTCMonth", new SetUTCMonth(globalContext));
        defineNonEnumerableProperty(proto, "setFullYear", new SetFullYear(globalContext));
        defineNonEnumerableProperty(proto, "setUTCFullYear", new SetUTCFullYear(globalContext));
        defineNonEnumerableProperty(proto, "toJSON", new ToJSON(globalContext));

        defineNonEnumerableProperty(proto, "getYear", new GetYear(globalContext));
        defineNonEnumerableProperty(proto, "setYear", new SetYear(globalContext));
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self == Types.UNDEFINED || self == Types.NULL) {
            DynDate now = new DynDate(context.getGlobalContext());
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
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinDate.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Date>";
    }
}
