package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;

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

}
