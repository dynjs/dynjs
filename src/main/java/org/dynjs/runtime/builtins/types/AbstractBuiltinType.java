package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.*;

public abstract class AbstractBuiltinType extends AbstractNativeFunction {

    public AbstractBuiltinType(GlobalContext globalContext, String... formalParameters) {
        super(globalContext, true, formalParameters);
    }

    public void initialize(GlobalContext globalContext) {
        Object proto = get(null, "prototype");
        if (proto == Types.UNDEFINED) {
            proto = null;
        }
        initialize(globalContext, (JSObject) proto);
    }

    public abstract void initialize(GlobalContext globalContext, JSObject prototype);

    protected void setPrototypeProperty(final JSObject prototype) {
        defineOwnProperty(null, "prototype",
                PropertyDescriptor.newDataPropertyDescriptor(prototype, false, false, false), false);
    }

    protected void defineNonEnumerableProperty(JSObject target, String name, final Object value) {
        target.defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, true, true, false), false);
    }
}
