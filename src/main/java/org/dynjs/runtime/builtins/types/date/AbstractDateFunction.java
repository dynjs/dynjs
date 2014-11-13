package org.dynjs.runtime.builtins.types.date;

import java.util.Date;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public abstract class AbstractDateFunction extends AbstractNativeFunction {

    protected static int MS_PER_DAY = 86400000;
    protected static int MS_PER_MINUTE = 60 * 1000;
    protected static int MS_PER_HOUR = MS_PER_MINUTE * 60;

    public AbstractDateFunction(GlobalContext globalContext, String... formalParams) {
        super(globalContext, formalParams);
    }

    protected static long day(long t) {
        return (long) Math.floor(t / MS_PER_DAY);
    }

    protected static int inLeapYear(long t) {
        if (daysInYear(yearFromTime(t)) == 365) {
            return 0;
        }

        return 1;
    }

    protected static int daysInYear(long y) {
        if (y % 4 != 0) {
            return 365;
        }
        if (y % 4 == 0 && y % 100 != 0) {
            return 366;
        }
        if (y % 100 == 0 && y % 400 != 0) {
            return 365;
        }
        if (y % 400 == 0) {
            return 366;
        }

        return 365;
    }

    protected static long dayFromYear(long y) {
        return (long) (365L * (y - 1970L) +
                Math.floor((y - 1969) / 4) -
                Math.floor((y - 1901) / 100) + Math.floor((y - 1601) / 400));
    }

    protected static long timeFromYear(long y) {
        return MS_PER_DAY * dayFromYear(y);
    }

    protected static long timeWithinDay(long t) {
        return t % MS_PER_DAY;
    }

    protected static long yearFromTime(long t) {
        long candidate = (long) Math.floor(t / (MS_PER_DAY * 365.25)) + 1970;

        while (timeFromYear(candidate) + MS_PER_DAY * daysInYear(candidate) <= t) {
            ++candidate;
        }

        while (timeFromYear(candidate) > t) {
            --candidate;
        }

        return candidate;
    }

    protected static int dayWithinYear(long t) {
        return (int) (day(t) - dayFromYear(yearFromTime(t)));
    }

    protected static int monthFromTime(long t) {
        int d = dayWithinYear(t);
        int leap = inLeapYear(t);

        if (d >= 0 && d < 31) {
            return 0;
        }
        if (d >= 31 && d < 59 + leap) {
            return 1;
        }
        if (d >= 59 + leap && d < 90 + leap) {
            return 2;
        }
        if (d >= 90 + leap && d < 120 + leap) {
            return 3;
        }
        if (d >= 120 + leap && d < 151 + leap) {
            return 4;
        }
        if (d >= 151 + leap && d < 181 + leap) {
            return 5;
        }
        if (d >= 181 + leap && d < 212 + leap) {
            return 6;
        }
        if (d >= 212 + leap && d < 243 + leap) {
            return 7;
        }
        if (d >= 243 + leap && d < 273 + leap) {
            return 8;
        }
        if (d >= 273 + leap && d < 304 + leap) {
            return 9;
        }
        if (d >= 304 + leap && d < 334 + leap) {
            return 10;
        }
        if (d >= 334 + leap && d < 365 + leap) {
            return 11;
        }

        return -1;
    }

    protected static int dateFromTime(long t) {
        int d = dayWithinYear(t);
        int m = monthFromTime(t);
        int leap = inLeapYear(t);

        switch (m) {
        case 0:
            return d + 1;
        case 1:
            return d - 30;
        case 2:
            return d - 58 - leap;
        case 3:
            return d - 89 - leap;
        case 4:
            return d - 119 - leap;
        case 5:
            return d - 150 - leap;
        case 6:
            return d - 180 - leap;
        case 7:
            return d - 211 - leap;
        case 8:
            return d - 242 - leap;
        case 9:
            return d - 272 - leap;
        case 10:
            return d - 303 - leap;
        case 11:
            return d - 333 - leap;
        }

        return -1;
    }

    protected static int dayFromMonth(long year, int m) {
        int leap = inLeapYear(timeFromYear(year));
        switch (m) {
        case 0:
            return 0;
        case 1:
            return 31;
        case 2:
            return 59 + leap;
        case 3:
            return 90 + leap;
        case 4:
            return 120 + leap;
        case 5:
            return 151 + leap;
        case 6:
            return 181 + leap;
        case 7:
            return 212 + leap;
        case 8:
            return 243 + leap;
        case 9:
            return 273 + leap;
        case 10:
            return 304 + leap;
        case 11:
            return 334 + leap;
        }

        return -1;
    }

    protected static int weekday(long t) {
        return (int) ((day(t) + 4) % 7);
    }

    protected static int hourFromTime(long t) {
        return (int) (Math.floor(t / MS_PER_HOUR) % 24);
    }

    protected static int minFromTime(long t) {
        return (int) Math.floor(t / MS_PER_MINUTE) % 60;
    }

    protected static int secFromTime(long t) {
        return (int) Math.floor(t / 1000) % 60;
    }

    protected static int msFromTime(long t) {
        return (int) (t % 1000);
    }

    public static Number makeTime(ExecutionContext context, Number hours, Number minutes, Number seconds, Number milliseconds) {
        if (!allAreFinite(hours, minutes, seconds, milliseconds)) {
            return Double.NaN;
        }

        long h = Types.toInteger(context, hours);
        long m = Types.toInteger(context, minutes);
        long s = Types.toInteger(context, seconds);
        long ms = Types.toInteger(context, milliseconds);

        long t = (h * MS_PER_HOUR) + (m * MS_PER_MINUTE) + (s * 1000) + ms;

        return t;
    }

    public static Number makeDay(ExecutionContext context, Number year, Number month, Number date) {
        if (!allAreFinite(year, month, date)) {
            return Double.NaN;
        }

        long y = Types.toInteger(context, year);
        long m = Types.toInteger(context, month);
        long d = Types.toInteger(context, date);

        long ym = (long) (Math.floor(m / 12));

        y += ym;
        m %= 12; // bound month with 0-11;

        if (m < 0) {
            m += 12; // turn negative idea of month into positive
        }
        
        long yd = dayFromYear(y);
        long md = dayFromMonth(y, (int) m);
        
        long t = yd + md + d - 1;
        
        return t;
    }

    public static Number makeDate(ExecutionContext context, Number day, Number time) {
        if (!allAreFinite(day, time)) {
            return Double.NaN;
        }

        return (day.longValue() * MS_PER_DAY) + time.longValue();
    }

    public static Number timeClip(ExecutionContext context, Number time) {
        if (!allAreFinite(time)) {
            return Double.NaN;
        }

        if (Math.abs(time.longValue()) > (8.64 * Math.pow(10, 15))) {
            return Double.NaN;
        }

        return Types.toInteger(context, time);
    }

    public static long localTime(ExecutionContext context, long t) {
        return t + localTza(context) + dst(context, t);
    }

    public static Number utc(ExecutionContext context, Number t) {
        if (allAreFinite(t)) {
            return utc(context, t.longValue());
        }

        return Double.NaN;
    }

    protected static long utc(ExecutionContext context, long t) {
        return t - localTza(context) - dst(context, t - localTza(context));
    }

    protected static int localTza(ExecutionContext context) {
        return context.getTimeZone().getRawOffset();
    }

    protected static int dst(ExecutionContext context, long t) {
        if (context.getTimeZone().inDaylightTime(new Date(t))) {
            return context.getTimeZone().getDSTSavings();
        }
        return 0;
    }

    public static boolean allAreFinite(Number... nums) {
        for (int i = 0; i < nums.length; ++i) {
            if (Double.isInfinite(nums[i].doubleValue()) || Double.isNaN(nums[i].doubleValue())) {
                return false;
            }
        }

        return true;
    }
}
