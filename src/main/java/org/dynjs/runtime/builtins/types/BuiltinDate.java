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

        this.forceDefineNonEnumerableProperty("now", new Now(globalObject) );
        this.forceDefineNonEnumerableProperty("parse", new Parse(globalObject));
        this.forceDefineNonEnumerableProperty("UTC", new UTC(globalObject) );
        
        proto.forceDefineNonEnumerableProperty("constructor", this);
        proto.forceDefineNonEnumerableProperty("toString", new ToString(globalObject));
        proto.forceDefineNonEnumerableProperty("toDateString", new ToDateString(globalObject));
        proto.forceDefineNonEnumerableProperty("toTimeString", new ToTimeString(globalObject));
        proto.forceDefineNonEnumerableProperty("toLocaleString", new ToLocaleString(globalObject));
        proto.forceDefineNonEnumerableProperty("toLocaleDateString", new ToLocaleDateString(globalObject));
        proto.forceDefineNonEnumerableProperty("toLocaleTimeString", new ToLocaleTimeString(globalObject));
        proto.forceDefineNonEnumerableProperty("toISOString", new ToISOString(globalObject));
        proto.forceDefineNonEnumerableProperty("toUTCString", new ToUTCString(globalObject));
        proto.forceDefineNonEnumerableProperty("toGMTString", new ToUTCString(globalObject));
        proto.forceDefineNonEnumerableProperty("valueOf", new ValueOf(globalObject));
        proto.forceDefineNonEnumerableProperty("getTime", new ValueOf(globalObject));
        proto.forceDefineNonEnumerableProperty("getFullYear", new GetFullYear(globalObject));
        proto.forceDefineNonEnumerableProperty("getMonth", new GetMonth(globalObject));
        proto.forceDefineNonEnumerableProperty("getDay", new GetDay(globalObject));
        proto.forceDefineNonEnumerableProperty("getHours", new GetHours(globalObject));
        proto.forceDefineNonEnumerableProperty("getMinutes", new GetMinutes(globalObject));
        proto.forceDefineNonEnumerableProperty("getSeconds", new GetSeconds(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCFullYear", new GetUTCFullYear(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCMonth", new GetUTCMonth(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCDay", new GetUTCDay(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCHours", new GetUTCHours(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCMinutes", new GetUTCMinutes(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCSeconds", new GetUTCSeconds(globalObject));
        proto.forceDefineNonEnumerableProperty("getTimezoneOffset", new GetTimezoneOffset(globalObject));
        proto.forceDefineNonEnumerableProperty("getMilliseconds", new GetMilliseconds(globalObject));
        proto.forceDefineNonEnumerableProperty("setMilliseconds", new SetMilliseconds(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCMilliseconds", new GetUTCMilliseconds(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCMilliseconds", new SetUTCMilliseconds(globalObject));
        proto.forceDefineNonEnumerableProperty("setTime", new SetTime(globalObject));
        proto.forceDefineNonEnumerableProperty("setSeconds", new SetSeconds(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCSeconds", new SetUTCSeconds(globalObject));
        proto.forceDefineNonEnumerableProperty("setMinutes", new SetMinutes(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCMinutes", new SetUTCMinutes(globalObject));
        proto.forceDefineNonEnumerableProperty("setHours", new SetHours(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCHours", new SetUTCHours(globalObject));
        proto.forceDefineNonEnumerableProperty("setDate", new SetDate(globalObject));
        proto.forceDefineNonEnumerableProperty("getDate", new GetDate(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCDate", new SetUTCDate(globalObject));
        proto.forceDefineNonEnumerableProperty("getUTCDate", new GetUTCDate(globalObject));
        proto.forceDefineNonEnumerableProperty("setMonth", new SetMonth(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCMonth", new SetUTCMonth(globalObject));
        proto.forceDefineNonEnumerableProperty("setFullYear", new SetFullYear(globalObject));
        proto.forceDefineNonEnumerableProperty("setUTCFullYear", new SetUTCFullYear(globalObject));
        proto.forceDefineNonEnumerableProperty("toJSON", new ToJSON(globalObject));

        proto.forceDefineNonEnumerableProperty("getYear", new GetYear(globalObject));
        proto.forceDefineNonEnumerableProperty("setYear", new SetYear(globalObject));
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
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinDate.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Date>";
    }
}
