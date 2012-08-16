package org.dynjs.runtime.conversion;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.Types;

public class ToPrimitive {
    public static Object toPrimitive(Object o) {
        if (o instanceof Types.Undefined) {
            return toPrimitive((Types.Undefined) o);
        }

        if (o instanceof Types.Null) {
            return toPrimitive((Types.Null) o);
        }

        if (o instanceof Boolean) {
            return toPrimitive((Boolean) o);
        }

        if (o instanceof Double) {
            return toPrimitive((Double) o);
        }

        if (o instanceof String) {
            return toPrimitive((String) o);
        }

        if (o instanceof DynObject) {
            return toPrimitive((DynObject) o);
        }

        return null;
    }

    public static Object toPrimitive(Types.Undefined undefined) {
        return undefined;
    }

    public static Object toPrimitive(Types.Null nil) {
        return nil;
    }

    public static Object toPrimitive(Boolean b) {
        return b;
    }

    public static Object toPrimitive(Double d) {
        return d;
    }

    public static Object toPrimitive(String s) {
        return s;
    }

    public static Object toPrimitive(DynObject object) {
        return object;
    }
}
