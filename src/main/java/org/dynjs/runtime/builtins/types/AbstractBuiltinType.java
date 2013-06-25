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
        forceDefineOwnProperty("prototype", new PropertyDescriptor() {
            {
                set("Value", prototype);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        });
    }

    /*
    protected void defineNonEnumerableProperty(JSObject target, String name, final Object value) {
        target.forceDefineOwnProperty(name, new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", true);
                set("Configurable", true);
                set("Enumerable", false);
            }
        });
    }
    
    protected void defineReadOnlyProperty(final JSObject target, final String name, final Object value) {
        target.forceDefineOwnProperty(name, new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", false);
                set("Enumerable", false);
                set("Configurable", false);
            }
        } );
    }
    */
}
