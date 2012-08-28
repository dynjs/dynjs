package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.object.Freeze;
import org.dynjs.runtime.builtins.types.object.GetOwnPropertyDescriptor;
import org.dynjs.runtime.builtins.types.object.GetOwnPropertyNames;
import org.dynjs.runtime.builtins.types.object.GetPrototypeOf;

public class BuiltinObject extends AbstractNativeFunction {

    public BuiltinObject(final GlobalObject globalObject) {
        super(globalObject, "value");

        defineOwnProperty(null, "freeze", new PropertyDescriptor() {
            {
                set( "Value", new Freeze(globalObject));
            }
        }, false);
        defineOwnProperty(null, "getPrototypeOf", new PropertyDescriptor() {
            {
                set( "Value", new GetPrototypeOf(globalObject));
            }
        }, false);
        defineOwnProperty(null, "getOwnPropertyDescriptor", new PropertyDescriptor() {
            {
                set( "Value", new GetOwnPropertyDescriptor(globalObject));
            }
        }, false);
        defineOwnProperty(null, "getOwnPropertyNames", new PropertyDescriptor() {
            {
                set( "Value", new GetOwnPropertyNames(globalObject));
            }
        }, false);
        DynObject proto = new DynObject();
        setPrototype(proto);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self != null) {
            if (args[0] instanceof JSObject) {
                return args[0];
            }

            if (args[0] instanceof String || args[0] instanceof Boolean || args[0] instanceof Number) {
                return Types.toObject(args[0]);
            }
        }

        return null;
    }

}
