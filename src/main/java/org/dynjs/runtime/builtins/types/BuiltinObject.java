package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.object.Create;
import org.dynjs.runtime.builtins.types.object.DefineProperties;
import org.dynjs.runtime.builtins.types.object.DefineProperty;
import org.dynjs.runtime.builtins.types.object.Freeze;
import org.dynjs.runtime.builtins.types.object.GetOwnPropertyDescriptor;
import org.dynjs.runtime.builtins.types.object.GetOwnPropertyNames;
import org.dynjs.runtime.builtins.types.object.GetPrototypeOf;
import org.dynjs.runtime.builtins.types.object.IsExtensible;
import org.dynjs.runtime.builtins.types.object.IsFrozen;
import org.dynjs.runtime.builtins.types.object.IsSealed;
import org.dynjs.runtime.builtins.types.object.Keys;
import org.dynjs.runtime.builtins.types.object.PreventExtensions;
import org.dynjs.runtime.builtins.types.object.Seal;

public class BuiltinObject extends AbstractNativeFunction {

    public BuiltinObject(final GlobalObject globalObject) {
        super(globalObject, "value");

        defineOwnProperty(null, "getPrototypeOf", new PropertyDescriptor() {
            {
                set("Value", new GetPrototypeOf(globalObject));
            }
        }, false);
        defineOwnProperty(null, "getOwnPropertyDescriptor", new PropertyDescriptor() {
            {
                set("Value", new GetOwnPropertyDescriptor(globalObject));
            }
        }, false);
        defineOwnProperty(null, "getOwnPropertyNames", new PropertyDescriptor() {
            {
                set("Value", new GetOwnPropertyNames(globalObject));
            }
        }, false);
        defineOwnProperty(null, "create", new PropertyDescriptor() {
            {
                set("Value", new Create(globalObject));
            }
        }, false);
        defineOwnProperty(null, "defineProperty", new PropertyDescriptor() {
            {
                set("Value", new DefineProperty(globalObject));
            }
        }, false);
        defineOwnProperty(null, "defineProperties", new PropertyDescriptor() {
            {
                set("Value", new DefineProperties(globalObject));
            }
        }, false);
        defineOwnProperty(null, "seal", new PropertyDescriptor() {
            {
                set("Value", new Seal(globalObject));
            }
        }, false);
        defineOwnProperty(null, "freeze", new PropertyDescriptor() {
            {
                set("Value", new Freeze(globalObject));
            }
        }, false);
        defineOwnProperty(null, "preventExtensions", new PropertyDescriptor() {
            {
                set("Value", new PreventExtensions(globalObject));
            }
        }, false);
        defineOwnProperty(null, "isSealed", new PropertyDescriptor() {
            {
                set("Value", new IsSealed(globalObject));
            }
        }, false);
        defineOwnProperty(null, "isFrozen", new PropertyDescriptor() {
            {
                set("Value", new IsFrozen(globalObject));
            }
        }, false);
        defineOwnProperty(null, "isExtensible", new PropertyDescriptor() {
            {
                set("Value", new IsExtensible(globalObject));
            }
        }, false);
        defineOwnProperty(null, "keys", new PropertyDescriptor() {
            {
                set("Value", new Keys(globalObject));
            }
        }, false);
        
        final DynObject proto = new DynObject();
        defineOwnProperty(null, "prototype", new PropertyDescriptor() {
            {
                set( "Value", proto );
            }
        }, false);

        setPrototype(globalObject.getPrototypeFor("Function"));
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
