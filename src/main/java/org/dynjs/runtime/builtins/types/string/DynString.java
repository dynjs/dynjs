package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class DynString extends PrimitiveDynObject {

    public DynString(GlobalContext globalContext) {
        this(globalContext, null);
    }

    public DynString(GlobalContext globalContext, String value) {
        super(globalContext, value);
        setClassName("String");
        setPrototype(globalContext.getPrototypeFor("String"));
    }

    @Override
    public void setPrimitiveValue(final Object value) {
        // 15.5.5.1
        super.setPrimitiveValue(value);
        String str = (String) value;
        int length = str.length();
        defineOwnProperty(null, "length",
                PropertyDescriptor.newDataPropertyDescriptor((long) length, false, false, false), false);

        for (int i = 0; i < length; i++) {
            defineOwnProperty(null, "" + i,
                    PropertyDescriptor.newDataPropertyDescriptor(str.substring(i, i + 1), false, false, true), false);
        }
    }

    @Override
    public Object getOwnProperty(ExecutionContext context, String name, boolean dupe) {
        Object d = super.getOwnProperty(context, name, dupe);
        if (d != Types.UNDEFINED) {
            return d;
        }

        if (!Types.toString(context, Math.abs(Types.toInteger(context, name))).equals(name)) {
            return Types.UNDEFINED;
        }

        String str = (String) getPrimitiveValue();
        long index = Types.toInteger(context, name);

        if (str.length() <= index) {
            return Types.UNDEFINED;
        }

        final String resultStr = str.substring((int) index, (int) index + 1);

        return PropertyDescriptor.newDataPropertyDescriptor(resultStr, false, false, true);
    }

}
