package org.dynjs.runtime.builtins.types;

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
import org.dynjs.runtime.builtins.types.object.prototype.ValueOf;

public class BuiltinObject extends AbstractBuiltinType {

    public BuiltinObject(final GlobalObject globalObject) {
        super(globalObject, "value");

        final JSObject proto = new DynObject(globalObject);
        setPrototypeProperty( proto );
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        // Object.prototype.foo()
        proto.forceDefineNonEnumerableProperty("constructor", this );
        
        proto.forceDefineNonEnumerableProperty("toString", new ToString(globalObject) );
        proto.forceDefineNonEnumerableProperty("toLocaleString", new ToLocaleString(globalObject) );
        proto.forceDefineNonEnumerableProperty("hasOwnProperty", new HasOwnProperty(globalObject) );
        proto.forceDefineNonEnumerableProperty("isPrototypeOf", new IsPrototypeOf(globalObject) );
        proto.forceDefineNonEnumerableProperty("propertyIsEnumerable", new PropertyIsEnumerable(globalObject) );
        proto.forceDefineNonEnumerableProperty("valueOf", new ValueOf(globalObject) );

        // Object.foo
        this.forceDefineNonEnumerableProperty("getPrototypeOf", new GetPrototypeOf(globalObject) );
        this.forceDefineNonEnumerableProperty("getOwnPropertyDescriptor", new GetOwnPropertyDescriptor(globalObject) );
        this.forceDefineNonEnumerableProperty("getOwnPropertyNames", new GetOwnPropertyNames(globalObject) );
        this.forceDefineNonEnumerableProperty("create", new Create(globalObject) );
        this.forceDefineNonEnumerableProperty("defineProperty", new DefineProperty(globalObject) );
        this.forceDefineNonEnumerableProperty("defineProperties", new DefineProperties(globalObject) );
        this.forceDefineNonEnumerableProperty("seal", new Seal(globalObject) );
        this.forceDefineNonEnumerableProperty("freeze", new Freeze(globalObject) );
        this.forceDefineNonEnumerableProperty("preventExtensions", new PreventExtensions(globalObject) );
        this.forceDefineNonEnumerableProperty("isSealed", new IsSealed(globalObject) );
        this.forceDefineNonEnumerableProperty("isFrozen", new IsFrozen(globalObject) );
        this.forceDefineNonEnumerableProperty("isExtensible", new IsExtensible(globalObject) );
        this.forceDefineNonEnumerableProperty("keys", new Keys(globalObject) );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {

        if ( args[0] != Types.UNDEFINED ) {
            if (args[0] instanceof JSObject) {
                return args[0];
            }
            if (args[0] instanceof String || args[0] instanceof Boolean || args[0] instanceof Number) {
                JSObject result = Types.toObject(context, args[0]);
                return result;
            }
        }

        return new DynObject(context.getGlobalObject() );
    }

    public static DynObject newObject(ExecutionContext context) {
        BuiltinObject ctor = (BuiltinObject) context.getGlobalObject().get(context, "__Builtin_Object");
        return (DynObject) context.construct(ctor);
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinObject.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Object>";
    }

}
