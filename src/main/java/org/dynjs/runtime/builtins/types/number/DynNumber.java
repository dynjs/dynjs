package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynNumber extends PrimitiveDynObject {

    public DynNumber(GlobalContext globalContext) {
        this(globalContext, null);
    }

    public DynNumber(GlobalContext globalContext, Number value) {
        super(globalContext, value);
        setClassName("Number");
        setPrototype(globalContext.getPrototypeFor("Number"));
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

}
