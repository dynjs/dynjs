package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
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
import org.dynjs.runtime.builtins.types.object.prototype.HasOwnProperty;
import org.dynjs.runtime.builtins.types.object.prototype.IsPrototypeOf;
import org.dynjs.runtime.builtins.types.object.prototype.PropertyIsEnumerable;
import org.dynjs.runtime.builtins.types.object.prototype.ToLocaleString;
import org.dynjs.runtime.builtins.types.object.prototype.ToString;

public class BuiltinObject extends AbstractNativeFunction {

    public BuiltinObject(final GlobalObject globalObject) {
        super(globalObject, "value");

        put(null, "getPrototypeOf", new GetPrototypeOf(globalObject), false );
        put(null, "getOwnPropertyDescriptor", new GetOwnPropertyDescriptor(globalObject), false );
        put(null, "getOwnPropertyNames", new GetOwnPropertyNames(globalObject), false );
        put(null, "create", new Create(globalObject), false );
        put(null, "defineProperty", new DefineProperty(globalObject), false );
        put(null, "defineProperties", new DefineProperties(globalObject), false );
        put(null, "seal", new Seal(globalObject), false );
        put(null, "freeze", new Freeze(globalObject), false );
        put(null, "preventExtensions", new PreventExtensions(globalObject), false );
        put(null, "isSealed", new IsSealed(globalObject), false );
        put(null, "isFrozen", new IsFrozen(globalObject), false );
        put(null, "isExtensible", new IsExtensible(globalObject), false );
        put(null, "keys", new Keys(globalObject), false );
        
        final DynObject proto = new DynObject( globalObject );
        proto.put( null, "toString", new ToString(globalObject), false );
        proto.put( null, "toLocaleString", new ToLocaleString(globalObject), false );
        proto.put( null, "hasOwnProperty", new HasOwnProperty(globalObject), false );
        proto.put( null, "isPrototypeOf", new IsPrototypeOf(globalObject), false );
        proto.put( null, "propertyIsEnumerable", new PropertyIsEnumerable(globalObject), false );
        put(null, "prototype", proto, false );
        
        setPrototype(globalObject.getPrototypeFor("Function"));
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self != null) {
            if (args[0] instanceof JSObject) {
                return args[0];
            }

            if (args[0] instanceof String || args[0] instanceof Boolean || args[0] instanceof Number) {
                return Types.toObject(context, args[0]);
            }
        }

        return null;
    }
    
    public static DynObject newObject(ExecutionContext context) {
        BuiltinObject ctor = (BuiltinObject) context.getGlobalObject().get(context, "Object");
        return (DynObject) context.construct(ctor);
    }

}
