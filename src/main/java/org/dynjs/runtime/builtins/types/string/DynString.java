package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
import org.dynjs.runtime.Types;

public class DynString extends PrimitiveDynObject {

    public DynString(GlobalObject globalObject) {
        this(globalObject, null);
    }

    public DynString(GlobalObject globalObject, String value) {
        super(globalObject, value);
        setClassName("String");
        setPrototype(globalObject.getPrototypeFor("String"));
    }

    @Override
    public void setPrimitiveValue(final Object value) {
        // 15.5.5.1
        super.setPrimitiveValue(value);
        String str = (String) value;
        int length = str.length();
        PropertyDescriptor lengthDesc = new PropertyDescriptor();
        lengthDesc.set(Names.VALUE, (long) length);
        lengthDesc.set(Names.WRITABLE, false);
        lengthDesc.set(Names.CONFIGURABLE, false);
        lengthDesc.set(Names.ENUMERABLE, false);
        defineOwnProperty(null, "length", lengthDesc, false);

        for (int i = 0; i < length; i++) {
            PropertyDescriptor desc = new PropertyDescriptor();
            desc.set(Names.VALUE, str.substring(i, i + 1));
            desc.set(Names.WRITABLE, false);
            desc.set(Names.CONFIGURABLE, false);
            desc.set(Names.ENUMERABLE, true);
            defineOwnProperty(null, "" + i, desc, false);
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

        PropertyDescriptor desc = new PropertyDescriptor();
        desc.set(Names.VALUE, resultStr);
        desc.set(Names.WRITABLE, false);
        desc.set(Names.CONFIGURABLE, false);
        desc.set(Names.ENUMERABLE, true);
        return desc;
    }

}
