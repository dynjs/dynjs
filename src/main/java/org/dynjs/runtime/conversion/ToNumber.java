package org.dynjs.runtime.conversion;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.Types;

import static org.dynjs.runtime.conversion.ToPrimitive.toPrimitive;

public class ToNumber {
    public static Double toNumber(Object o) {
        if (o instanceof Types.Undefined) {
            return toNumber((Types.Undefined) o);
        }

        if (o instanceof Types.Null) {
            return toNumber((Types.Null) o);
        }

        if (o instanceof Boolean) {
            return toNumber((Boolean) o);
        }

        if (o instanceof Double) {
            return toNumber((Double) o);
        }

        if (o instanceof String) {
            return toNumber((String) o);
        }

        if (o instanceof DynObject) {
            return toNumber((DynObject) o);
        }

        return null;
    }

    public static Double toNumber(Types.Undefined u) {
        return Double.NaN;
    }

    public static Double toNumber(Types.Null n) {
        return 0.0;
    }

    public static Double toNumber(Boolean b) {
        return b ? 1.0 : 0.0;
    }

    public static Double toNumber(Double d) {
        return d;
    }

    public static Double toNumber(String s) {
        if (s.isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return Double.NaN;
            }
        }
    }

    public static Double toNumber(DynObject o) {
        Object primValue = toPrimitive(o);

        return toNumber(primValue);
    }

}
