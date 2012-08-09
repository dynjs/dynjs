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
    
    static boolean isCallable(Object o) {
        return ( o instanceof JSCallable );
    }
    
    // ----------------------------------------------------------------------

    private static class Undefined {

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
    
    private static class Null {
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
