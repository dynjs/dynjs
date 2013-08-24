package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.PropertyDescriptor.Names;

public abstract class AbstractBuiltinType extends AbstractNativeFunction {

    public AbstractBuiltinType(GlobalObject globalObject, String... formalParameters) {
        super(globalObject, true, formalParameters);
    }

    public void initialize(GlobalObject globalObject) {
        Object proto = get(null, "prototype");
        if (proto == Types.UNDEFINED) {
            proto = null;
        }
        initialize(globalObject, (JSObject) proto);
    }

    public abstract void initialize(GlobalObject globalObject, JSObject prototype);

    protected void setPrototypeProperty(final JSObject prototype) {
        defineOwnProperty(null, "prototype", new PropertyDescriptor() {
            {
                set(Names.VALUE, prototype);
                set(Names.WRITABLE, false);
                set(Names.CONFIGURABLE, false);
                set(Names.ENUMERABLE, false);
            }
        }, false);
    }

    protected void defineNonEnumerableProperty(JSObject target, String name, final Object value) {
        target.defineOwnProperty(null, name, new PropertyDescriptor() {
            {
                set( Names.VALUE, value );
                set( Names.WRITABLE, true );
                set( Names.CONFIGURABLE, true );
                set( Names.ENUMERABLE, false );
            }
        }, false);
    }
}
