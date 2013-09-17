package org.dynjs.runtime;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.builtins.types.bool.DynBoolean;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.dynjs.runtime.builtins.types.string.DynString;

public class Types {

    public static final Undefined UNDEFINED = new Undefined();
    public static final Null NULL = new Null();

    public static void checkObjectCoercible(ExecutionContext context, Object o) {
        if (o == Types.UNDEFINED) {
            throw new ThrowException(context, context.createTypeError("undefined cannot be coerced to an object"));
        }

        if (o == Types.NULL) {
            throw new ThrowException(context, context.createTypeError("null cannot be coerced to an object"));
        }

        return;
    }

    public static void checkObjectCoercible(ExecutionContext context, Object o, String debug) {
        if (o == Types.UNDEFINED) {
            throw new ThrowException(context, context.createTypeError("undefined cannot be coerced to an object: " + debug));
        }

        if (o == Types.NULL) {
            throw new ThrowException(context, context.createTypeError("null cannot be coerced to an object: " + debug));
        }

        return;
    }

    public static boolean sameValue(Object left, Object right) {

        if (left.getClass() != right.getClass()) {
            return false;
        }

        if (left == UNDEFINED) {
            return true;
        }

        if (left == NULL) {
            return true;
        }

        return left.equals(right);
    }

    public static JSObject toObject(ExecutionContext context, Object o) {
        if (o instanceof JSObject) {
            return (JSObject) o;
        }
        if (o instanceof String) {
            return new DynString(context.getGlobalObject(), (String) o);
        }
        if (o instanceof Number) {
            return new DynNumber(context.getGlobalObject(), (Number) o);
        }
        if (o instanceof Boolean) {
            return new DynBoolean(context.getGlobalObject(), (Boolean) o);
        }
        if (o == Types.UNDEFINED) {
            throw new ThrowException(context, context.createTypeError("undefined cannot be converted to an object"));
        }
        if (o == Types.NULL) {
            throw new ThrowException(context, context.createTypeError("null cannot be converted to an object"));
        }
        return new PrimitiveDynObject(context.getGlobalObject(), o);
    }

    public static Object toThisObject(ExecutionContext context, Object o) {
        if (o instanceof JSObject) {
            return (JSObject) o;
        }
        if (o instanceof String) {
            return new DynString(context.getGlobalObject(), (String) o);
        }
        if (o instanceof Number) {
            return new DynNumber(context.getGlobalObject(), (Number) o);
        }
        if (o instanceof Boolean) {
            return new DynBoolean(context.getGlobalObject(), (Boolean) o);
        }
        if (o == Types.UNDEFINED) {
            throw new ThrowException(context, context.createTypeError("undefined cannot be converted to an object"));
        }
        if (o == Types.NULL) {
            throw new ThrowException(context, context.createTypeError("null cannot be converted to an object"));
        }
        // return new PrimitiveDynObject(context.getGlobalObject(), o);
        return o;
    }

    public static Object toPrimitive(ExecutionContext context, Object o) {
        return toPrimitive(context, o, null);
    }

    public static Object toPrimitive(ExecutionContext context, Object o, String preferredType) {
        // 9.1
        if (o instanceof JSObject) {
            return ((JSObject) o).defaultValue(context, preferredType);
        }
        return o;
    }

    public static Number toNumber(ExecutionContext context, Object o) {
        // 9.3
        if (o instanceof Number) {
            return (Number) o;
        }

        if (o instanceof JSObject) {
            return toNumber(context, toPrimitive(context, o, "Number"));
        }

        if (o == Types.UNDEFINED) {
            return Double.NaN;
        }

        if (o == Types.NULL) {
            return 0L;
        }

        if (o instanceof Boolean) {
            if (o == Boolean.TRUE) {
                return 1L;
            }

            return 0L;
        }

        Number n = stringToNumber(o.toString());
        return n;
    }

    public static String trimString(String value) {
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();

        while ((st < len) && (isWhitespace(val[st]))) {
            st++;
        }
        while ((st < len) && (isWhitespace(val[len - 1]))) {
            len--;
        }

        String result = null;
        
        if ( st > 0 || len < value.length() ) {
            result = value.substring(st,len);
        } else {
            result = value;
        }
        return result;
    }

    public static String trimNumericString(String value) {
        char[] val = value.toCharArray();

        StringBuilder newStr = new StringBuilder();

        for (int i = 0; i < val.length; ++i) {
            if (!isWhitespace(val[i])) {
                newStr.append(val[i]);
            }
        }

        return newStr.toString().trim();
    }

    public static boolean isWhitespace(char c) {
        switch (c) {
        case '\n':
        case '\r':
        case '\u0009':
        case '\u000B':
        case '\u000C':
        case '\u0020':
        case '\u00A0':
        case '\u1680':
        case '\u180E':
        case '\u2000':
        case '\u2001':
        case '\u2002':
        case '\u2003':
        case '\u2004':
        case '\u2005':
        case '\u2006':
        case '\u2007':
        case '\u2008':
        case '\u2009':
        case '\u200A':
        case '\u2028':
        case '\u2029':
        case '\u202F':
        case '\u205F':
        case '\u3000':
        case '\uFEFF':
            return true;

        }
        return false;
    }

    static final Pattern decimalDigitPattern = Pattern.compile(".*[0-9].*");
    public static Number stringToNumber(String str) {
        str = trimNumericString(str);

        if (str.equals("")) {
            return 0L;
        }

        if (str.equals("-0")) {
            return Double.valueOf(-0.0);
        }

        if (str.equals("Infinity") || str.equals("+Infinity")) {
            return Math.pow(10, 10000);
        }

        if (str.equals("-Infinity")) {
            return -Math.pow(10, 10000);
        }

        if (str.startsWith("0x") || str.startsWith("0X")) {
            return Long.decode(str);
        }

        // If we've come this far and there's not a decimal digit in
        // this string anywhere then bail out early
        if (!decimalDigitPattern.matcher(str).matches()) {
            return Double.NaN;
        }

        if ((str.indexOf("e") > 0) || (str.indexOf("E") > 0)) {
            if (str.indexOf(".") >= 0) {
                str = str.replaceFirst("[eE]", "E");
            } else {
                str = str.replaceFirst("[eE]", ".0E");
            }
        }

        try {
            if (str.indexOf(".") >= 0) {
                return Double.valueOf(str);
            } else {
                return Long.valueOf(str);
            }
        } catch (NumberFormatException e) {
            return Double.NaN;
        }

    }

    public static Boolean toBoolean(Object o) {
        // 9.2
        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        if (o == Types.UNDEFINED || o == Types.NULL) {
            return false;
        }
        if (o instanceof Double) {
            if (((Double) o).isNaN() || ((Double) o).doubleValue() == 0.0) {
                return false;
            }
            return true;
        }

        if (o instanceof Number) {
            if (((Number) o).intValue() == 0) {
                return false;
            }
            return true;
        }
        if (o instanceof String) {
            return (((String) o).length() != 0);
        }

        if (o instanceof JSObject) {
            return true;
        }

        return true;
    }

    public static Long toInteger(ExecutionContext context, Object o) {
        Number number = toNumber(context, o);
        if (number instanceof Double) {
            if (((Double) number).isNaN()) {
                return 0L;
            }
        }

        return number.longValue();
    }

    public static Long toUint16(ExecutionContext context, Object o) {
        // 9.7
        Number n = toNumber(context, o);

        if (n instanceof Double) {
            if (((Double) n).isInfinite() || ((Double) n).isNaN()) {
                return 0L;
            }
        }

        long posInt = (long) (sign(n) * Math.floor(Math.abs(n.longValue())));

        return modulo(posInt, 65536);
    }

    protected static int sign(Number n) {
        if (n.doubleValue() < 0) {
            return -1;
        }

        return 1;
    }

    public static Long toUint32(ExecutionContext context, Object o) {
        // 9.5
        Number n = toNumber(context, o);

        if (n instanceof Double) {
            if (((Double) n).isInfinite() || ((Double) n).isNaN() || ((Double) n).doubleValue() == -0) {
                return (long) 0;
            }
        }

        double d = n.doubleValue();

        long posInt = (long) ((sign(d)) * Math.floor(Math.abs(d)));

        long int32bit = modulo(posInt, 4294967296L);

        return (long) int32bit;
    }

    public static long modulo(long a, long b) {
        // because Java modulo doesn't deal with negatives the way the
        // javascript spec assumes it should.
        return (a % b + b) % b;
    }

    public static Long toInt32(ExecutionContext context, Object o) {
        Number number = toNumber(context, o);
        if (Double.isInfinite(number.doubleValue()) || Double.isNaN(number.doubleValue())) {
            return 0L;
        }

        double doubleNum = number.doubleValue();

        long posInt = (long) ((sign(doubleNum)) * Math.floor(Math.abs(doubleNum)));

        long int32bit = modulo(posInt, 4294967296L);

        if (int32bit >= 2147483648L) {
            int32bit = int32bit - 4294967296L;
        }
        return int32bit;
    }

    public static boolean isCallable(Object o) {
        return (o instanceof JSCallable);
    }

    public static boolean isSparse(ExecutionContext context, JSObject o) {
        if (!o.hasProperty(context, "length")) {
            return false;
        }

        long len = Types.toUint32(context, o.get(context, "length"));

        for (long i = 0; i < len; ++i) {
            if (o.getOwnProperty(context, "" + i, false) == Types.UNDEFINED) {
                return true;
            }
        }

        return false;
    }

    public static Object getValue(ExecutionContext context, Object o) {
        if (o instanceof Reference) {
            return ((Reference) o).getValue(context);
        }
        return o;
    }

    private static Pattern EXP_PATTERN = Pattern.compile("^(.*)e([+-])([0-9]+)$");
    public static String toString(ExecutionContext context, Object o) {
        if (o == Types.UNDEFINED) {
            return "undefined";
        }

        if (o == Types.NULL || o == null) {
            return "null";
        }

        if (o instanceof JSObject) {
            return (String) toString(context, toPrimitive(context, o, "String"));
        }
        if (o instanceof Number) {
            if (((Number) o).doubleValue() == -0) {
                return "0";
            }

            if (o instanceof Double) {
                Double dbl = (Double) o;
                if (dbl.doubleValue() == (long) dbl.doubleValue()) {
                    o = dbl.longValue();
                }
            }
            String result = Types.rewritePossiblyExponentialValue(o.toString());

            Matcher matcher = EXP_PATTERN.matcher(result);
            if (matcher.matches()) {
                int expValue = Integer.parseInt(matcher.group(3));
                if (matcher.group(2).equals("+")) {
                    if (expValue <= 20) {
                        String digits = matcher.group(1).replace(".", "");
                        StringBuilder newResult = new StringBuilder();
                        int offset = 0;
                        if (digits.startsWith("-")) {
                            newResult.append("-");
                            offset = 1;
                        }
                        for (int i = offset; i <= expValue; ++i) {
                            if (i >= digits.length()) {
                                newResult.append("0");
                            } else {
                                newResult.append(digits.charAt(i));
                            }
                        }
                        if (newResult.length() < digits.length()) {
                            newResult.append(".");
                            newResult.append(digits.substring(newResult.length() - 1));
                        }
                        result = newResult.toString();
                    }
                } else {
                    if (expValue <= 6) {
                        StringBuilder newResult = new StringBuilder().append("0.");
                        for (int i = 0; i < (expValue - 1); ++i) {
                            newResult.append(0);
                        }

                        String digits = matcher.group(1).replace(".", "");
                        if (digits.startsWith("-")) {
                            newResult.append(digits.substring(1));
                            newResult.insert(0, "-");
                        } else {
                            newResult.append(digits);
                        }
                        result = newResult.toString();
                    }
                }
            }
            return result;
        }
        return o.toString();
    }

    public static String typeof(ExecutionContext context, Object o) {
        // 11.4.3
        Object val = o;
        if (o instanceof Reference) {
            Reference r = (Reference) o;
            if (r.isUnresolvableReference()) {
                return "undefined";
            }
            val = getValue(context, o);
        }

        return type(val);
    }

    public static Object compareRelational(ExecutionContext context, Object x,
            Object y, boolean leftFirst) {
        // 11.8.5

        Object px = null;
        Object py = null;

        if (leftFirst) {
            px = toPrimitive(context, x, "Number");
            py = toPrimitive(context, y, "Number");
        } else {
            py = toPrimitive(context, y, "Number");
            px = toPrimitive(context, x, "Number");
        }

        if (px instanceof String && py instanceof String) {
            String sx = (String) px;
            String sy = (String) py;

            if (sx.compareTo(sy) < 0) {
                return true;
            }

            return false;
        } else {
            Number nx = toNumber(context, px);
            Number ny = toNumber(context, py);

            if (Double.isNaN(nx.doubleValue()) || Double.isNaN(ny.doubleValue())) {
                return Types.UNDEFINED;
            }

            if (nx.equals(ny)) {
                return false;
            }

            if (nx.doubleValue() == Double.POSITIVE_INFINITY) {
                return false;
            }

            if (ny.doubleValue() == Double.POSITIVE_INFINITY) {
                return true;
            }

            if (ny.doubleValue() == Double.NEGATIVE_INFINITY) {
                return false;
            }

            if (nx.doubleValue() == Double.NEGATIVE_INFINITY) {
                return true;
            }

            if (nx.doubleValue() < ny.doubleValue()) {
                return true;
            }

            return false;
        }

    }

    public static boolean compareEquality(ExecutionContext context, Object lhs, Object rhs) {
        // 11.9.3

        if (lhs.getClass().equals(rhs.getClass()) || (lhs instanceof Number && rhs instanceof Number)) {
            if (lhs == Types.UNDEFINED) {
                return true;
            }
            if (lhs == Types.NULL) {
                return true;
            }
            if (lhs instanceof Number) {
                if (lhs instanceof Double) {
                    if (((Double) lhs).isNaN()) {
                        return false;
                    }
                }
                if (rhs instanceof Double) {
                    if (((Double) rhs).isNaN()) {
                        return false;
                    }
                }
                if (((Number) lhs).doubleValue() == ((Number) rhs)
                        .doubleValue()) {
                    return true;
                }
                return false;
            }
            if (lhs instanceof String || lhs instanceof Boolean) {
                return lhs.equals(rhs);
            }
            if (lhs == rhs) {
                return true;
            }
        }

        if (lhs == Types.UNDEFINED && rhs == Types.NULL) {
            return true;
        }

        if (lhs == Types.NULL && rhs == Types.UNDEFINED) {
            return true;
        }

        if (lhs instanceof Number && rhs instanceof String) {
            return compareEquality(context, lhs, toNumber(context, rhs));
        }

        if (lhs instanceof String && rhs instanceof Number) {
            return compareEquality(context, toNumber(context, lhs), rhs);
        }

        if (lhs instanceof Boolean) {
            return compareEquality(context, toNumber(context, lhs), rhs);
        }

        if (rhs instanceof Boolean) {
            return compareEquality(context, lhs, toNumber(context, rhs));
        }

        if ((lhs instanceof String || lhs instanceof Number) && rhs instanceof JSObject) {
            return compareEquality(context, lhs, toPrimitive(context, rhs));
        }

        if (lhs instanceof JSObject && (rhs instanceof String || rhs instanceof Number)) {
            return compareEquality(context, toPrimitive(context, lhs), rhs);
        }

        return false;
    }

    public static boolean compareStrictEquality(ExecutionContext context, Object lhs, Object rhs) {
        // 11.9.6
        
        //System.err.println( "lhs: " + lhs.getClass() + " // " + lhs );
        //System.err.println( "rhs: " + rhs.getClass() + " // " + rhs );
        //System.err.println( "lhs: " + System.identityHashCode(lhs));
        //System.err.println( "rhs: " + System.identityHashCode(rhs));

        if (!lhs.getClass().equals(rhs.getClass())
                // Allow comparison of Doubles and Longs (because 0 === -0 in Javascript
                // go figure
                && !(lhs instanceof Number && rhs instanceof Number)) {
            return false;
        }

        if (lhs == UNDEFINED) {
            return true;
        }

        if (lhs == NULL) {
            return true;
        }

        if (lhs instanceof Number) {
            if (((Number) lhs).doubleValue() == Double.NaN) {
                return false;
            }
            if (rhs instanceof Number
                    && ((Number) rhs).doubleValue() == Double.NaN) {
                return false;
            }
            return ((Number) lhs).doubleValue() == ((Number) rhs).doubleValue();
        }

        if (lhs instanceof String || lhs instanceof Boolean) {
            return lhs.equals(rhs);
        }
        
        //System.err.println( "identity: " + ( lhs == rhs ));

        return lhs == rhs;
    }

    public static String type(Object o) {
        // 11.4.3
        if (o == UNDEFINED) {
            return "undefined";
        }

        if (o == NULL) {
            return "object";
        }

        if (o instanceof Boolean) {
            return "boolean";
        }

        if (o instanceof Number) {
            return "number";
        }

        if (o instanceof JSFunction) {
            return "function";
        }

        if (o instanceof JSObject) {
            return "object";
        }

        if (o instanceof String) {
            return "string";
        }

        return o.getClass().getName();

    }

    // ----------------------------------------------------------------------

    public static class Undefined {

        private Undefined() {
        }

        public String typeof() {
            return "undefined";
        }

        @Override
        public String toString() {
            return "undefined";
        }
    }

    public static class Null {
        private Null() {
        }

        public String typeof() {
            return "object";
        }

        @Override
        public String toString() {
            return "null";
        }
    }
    
    private static final Pattern POSSIBLE_EXP_REGEXP = Pattern.compile("^(.*?)(\\.\\d*)*E([+-])?(\\d+)");

    public static String rewritePossiblyExponentialValue(String value) {
        // Java writes exponential values as 1.0E14 while JS likes
        // them as 1e+14

        Matcher matcher = POSSIBLE_EXP_REGEXP.matcher(value);

        if (matcher.matches()) {
            String decimal = matcher.group(1);
            String fraction = matcher.group(2);
            String sign = matcher.group(3);
            String exponent = matcher.group(4);
            if (fraction == null || ".0".equals(fraction)) {
                fraction = "";
            }
            if (sign == null) {
                sign = "+";
            }
            return decimal + fraction + "e" + sign + exponent;
        }

        return value;
    }

    public static Number parseLongOrDouble(String text, int radix) {
        Double dbl;
        if (radix == 10) {
            dbl = Double.valueOf(text);
        } else {
            dbl = Double.valueOf(new BigInteger(text, radix).doubleValue());
        }
        if (dbl.doubleValue() == (long) dbl.doubleValue()) {
            return Long.valueOf(dbl.longValue());
        } else {
            return dbl;
        }
    }
}
