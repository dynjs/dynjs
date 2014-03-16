package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

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
        defineOwnProperty(null, "prototype",
                PropertyDescriptor.newDataPropertyDescriptor(prototype, false, false, false), false);
    }

    protected void defineNonEnumerableProperty(JSObject target, String name, final Object value) {
        target.defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, true, true, false), false);
    }
}
