package org.dynjs.runtime;

import org.dynjs.runtime.builtins.types.bool.DynBoolean;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.dynjs.runtime.builtins.types.string.DynString;

public class Types {

    public static final Undefined UNDEFINED = new Undefined();
    public static final Null NULL = new Null();

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
        return new PrimitiveDynObject(context.getGlobalObject(), o);
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
        if (o instanceof JSObject) {
            return toNumber(context, toPrimitive(context, o, "Number"));
        }

        if (o instanceof Number) {
            return (Number) o;
        }

        if (o == Types.UNDEFINED) {
            return Double.NaN;
        }

        if (o == Types.NULL) {
            return Double.valueOf(0);
        }

        if (o instanceof Boolean) {
            if (o == Boolean.TRUE) {
                return 1;
            }

            return 0;
        }

        try {
            String str = o.toString();
            if (str.equals("Infinity")) {
                return Math.pow(10, 10000);
            }
            if (str.trim().isEmpty()) {
                return 0;
            }
            if (str.indexOf(".") > 0) {
                return Double.valueOf(str);
            } else {
                return Integer.valueOf(str);
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

        if (o instanceof PrimitiveDynObject) {
            return toBoolean(((PrimitiveDynObject) o).getPrimitiveValue());
        }

        return true;
    }

    public static Integer toInteger(ExecutionContext context, Object o) {
        Number number = toNumber(context, o);
        if (number instanceof Double) {
            if (((Double) number).isNaN()) {
                return 0;
            }
        }

        return number.intValue();
    }

    public static Integer toUint16(ExecutionContext context, Object o) {
        // 9.7
        Number n = toNumber(context, o);
        
        if (n instanceof Double) {
            if (((Double) n).isInfinite() || ((Double) n).isNaN()) {
                return 0;
            }
        }
        
        int posInt = (int) (sign(n) * Math.floor( Math.abs( n.intValue() )));
        
        return (posInt % 65536) ;
    }
    
    protected static int sign(Number n) {
        if ( n.doubleValue() < 0 ) {
            return -1;
        }
        
        return 1;
    }

    public static Integer toUint32(ExecutionContext context, Object o) {
        // 9.5
        Number n = toNumber(context, o);

        if (n instanceof Integer) {
            return ((Integer) n).intValue();
        }

        if (n instanceof Double) {
            if (((Double) n).isInfinite() || ((Double) n).isNaN()) {
                return 0;
            }
        }

        double d = n.doubleValue();

        double posInt = (d < 0 ? -1 : 1) * Math.floor(Math.abs(d));

        double int32bit = posInt % Math.pow(2, 32);

        return (int) int32bit;
    }

    public static Integer toInt32(ExecutionContext context, Object o) {
        int int32bit = toUint32(context, o);
        if (int32bit > Math.pow(2, 31)) {
            return (int) (int32bit - Math.pow(2, 32));
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

        int len = Types.toUint32(context, o.get(context, "length"));

        for (int i = 0; i < len; ++i) {
            if (o.getOwnProperty(context, "" + i) == Types.UNDEFINED) {
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

    public static String toString(ExecutionContext context, Object o) {
        if (o instanceof JSObject) {
            return (String) toPrimitive(context, o, "String");
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

    public static Object compareRelational(ExecutionContext context, Object x, Object y, boolean leftFirst) {
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

            if (nx.doubleValue() == Double.NaN || ny.doubleValue() == Double.NaN) {
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

        String lhsType = type(lhs);
        String rhsType = type(rhs);

        if (lhsType.equals(rhsType)) {
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
                if (lhs.equals(rhs)) {
                    return true;
                }
                return false;
            }
            if (lhs instanceof String || lhs instanceof Boolean) {
                return lhs.equals(rhs);
            }
        }

        if (lhs == Types.UNDEFINED && rhs == Types.NULL) {
            return true;
        }

        if (lhs == Types.NULL && rhs == Types.UNDEFINED) {
            return true;
        }

        if (lhsType.equals("number") && rhsType.equals("string")) {
            return compareEquality(context, lhs, toNumber(context, rhs));
        }

        if (lhsType.equals("string") && rhsType.equals("number")) {
            return compareEquality(context, toNumber(context, lhs), rhs);
        }

        if (lhsType.equals("boolean")) {
            return compareEquality(context, toNumber(context, lhs), rhs);
        }

        if (rhsType.equals("boolean")) {
            return compareEquality(context, lhs, toNumber(context, rhs));
        }

        if ((lhsType.equals("string") || lhsType.equals("number")) && rhsType.equals("object")) {
            return compareEquality(context, lhs, toPrimitive(context, rhs));
        }

        if (lhsType.equals("object") && (rhsType.equals("string") || rhsType.equals("number"))) {
            return compareEquality(context, toPrimitive(context, lhs), rhs);
        }

        return false;
    }

    public static boolean compareStrictEquality(ExecutionContext context, Object lhs, Object rhs) {
        // 11.9.6
        String lhsType = type(lhs);
        String rhsType = type(rhs);

        if (!lhsType.equals(rhsType)) {
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
            if (rhs instanceof Number && ((Number) rhs).doubleValue() == Double.NaN) {
                return false;
            }
            return lhs.equals(rhs);
        }

        if (lhs instanceof String || lhs instanceof Boolean) {
            return lhs.equals(rhs);
        }

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
}
