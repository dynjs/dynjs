package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
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
        defineOwnProperty(null, "length", new PropertyDescriptor() {
            {
                set("Value", ((String) value).length());
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);
    }

    @Override
    public Object getOwnProperty(ExecutionContext context, String name) {
        Object d = super.getOwnProperty(context, name);
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

        return new PropertyDescriptor() {
            {
                set( "Value", resultStr );
                set( "Writable", false );
                set( "Configurable", false );
                set( "Enumerable", true );
            }
        };
    }

}
