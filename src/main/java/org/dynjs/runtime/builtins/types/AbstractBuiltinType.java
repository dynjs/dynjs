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
        PropertyDescriptor prototypeDesc = new PropertyDescriptor();
        prototypeDesc.set(Names.VALUE, prototype);
        prototypeDesc.set(Names.WRITABLE, false);
        prototypeDesc.set(Names.CONFIGURABLE, false);
        prototypeDesc.set(Names.ENUMERABLE, false);
        defineOwnProperty(null, "prototype", prototypeDesc, false);
    }

    protected void defineNonEnumerableProperty(JSObject target, String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor();
        desc.set( Names.VALUE, value );
        desc.set( Names.WRITABLE, true );
        desc.set( Names.CONFIGURABLE, true );
        desc.set( Names.ENUMERABLE, false );
        target.defineOwnProperty(null, name, desc, false);
    }
}
