package org.dynjs.runtime;

import org.dynjs.exception.DynJSException;

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

        return left.equals( right );
    }

    static JSObject toObject(Object o) {
        if (o instanceof JSObject) {
            return (JSObject) o;
        }
        throw new DynJSException( "not implemented" );
    }

    static Object toPrimitive(Object o, String preferredType) {
        if (o instanceof JSObject) {
            return ((JSObject) o).defaultValue( preferredType );
        }
        return o;
    }

    static Double toNumber(Object o) {
        // 9.3
        if (o instanceof JSObject) {
            return toNumber( toPrimitive( o, "Number" ) );
        }

        if (o instanceof Double) {
            return (Double) o;
        }

        if (o == Types.UNDEFINED) {
            return Double.NaN;
        }

        if (o == Types.NULL) {
            return Double.valueOf( 0 );
        }

        try {
            return Double.valueOf( o.toString() );
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    static Boolean toBoolean(Object o) {
        // 9.2
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        if (o == Types.UNDEFINED || o == Types.NULL) {
            return false;
        }
        if (o instanceof Number) {
            double d = ((Number) o).doubleValue();
            if (d == 0 || d == Double.NaN) {
                return false;
            }
            return true;
        }
        if (o instanceof String) {
            return (((String) o).length() != 0);
        }

        return true;
    }

    static Double toUint32(Object o) {
        // 9.5
        Double n = toNumber( o );
        if (n == Double.POSITIVE_INFINITY || n == Double.NEGATIVE_INFINITY || n == Double.NaN) {
            return 0.0;
        }

        double posInt = (n < 0 ? -1 : 1) * Math.floor( Math.abs( n ) );

        double int32bit = posInt % Math.pow( 2, 32 );

        return int32bit;
    }

    static Double toInt32(Object o) {
        double int32bit = toUint32( o );
        if (int32bit > Math.pow( 2, 31 )) {
            return int32bit - Math.pow( 2, 32 );
        }
        return int32bit;
    }

    static boolean isCallable(Object o) {
        return (o instanceof JSCallable);
    }

    static Object getValue(ExecutionContext context, Object o) {
        if (o instanceof Reference) {
            return ((Reference) o).getValue( context );
        }
        return o;
    }

    static String toString(Object o) {
        return o.toString();
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
