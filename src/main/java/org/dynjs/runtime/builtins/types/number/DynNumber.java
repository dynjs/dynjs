package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynNumber extends PrimitiveDynObject {

    public DynNumber(GlobalObject globalObject) {
        this(globalObject, null);
    }

    public DynNumber(GlobalObject globalObject, Number value) {
        super(globalObject, value);
        setClassName("Number");
        setPrototype(globalObject.getPrototypeFor("Number"));
    }

    public static boolean isNaN(Object object) {
        if (object instanceof Double) {
            return Double.isNaN((Double) object);
        } else if (object instanceof Integer) {
            return false;
        } else if (object instanceof DynNumber) {
            return DynNumber.isNaN(((DynNumber) object).getPrimitiveValue());
        }
        return false;
    }

    public static String rewritePossiblyExponentialValue(String value) {
        // Java writes exponential values as 1.0E14 while JS likes
        // them as 1e+14
        int plus = 3;
        int index = value.indexOf(".0E");
        if (index < 0) { index = value.indexOf("E+"); plus = 2; }
        if (index != -1)
            value = value.substring(0, index) + "e+" + value.substring(index + plus);
        return value;
    }

}