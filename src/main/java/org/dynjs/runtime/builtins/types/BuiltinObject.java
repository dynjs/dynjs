package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.object.*;
import org.dynjs.runtime.builtins.types.object.prototype.*;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.DefineGetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.DefineSetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.LookupGetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.LookupSetter;

public class BuiltinObject extends AbstractBuiltinType {

    public BuiltinObject(final GlobalObject globalObject) {
        super(globalObject, "value");

        final JSObject proto = new DynObject(globalObject);
        setPrototypeProperty( proto );
    }

    @Override
    public void initialize(GlobalObject globalObject, final JSObject proto) {
        // Object.prototype.foo()
        defineNonEnumerableProperty(proto, "constructor", this );
        
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject) );
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalObject) );
        defineNonEnumerableProperty(proto, "hasOwnProperty", new HasOwnProperty(globalObject) );
        defineNonEnumerableProperty(proto, "isPrototypeOf", new IsPrototypeOf(globalObject) );
        defineNonEnumerableProperty(proto, "propertyIsEnumerable", new PropertyIsEnumerable(globalObject) );
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalObject) );
        if (globalObject.getConfig().isRhinoCompatible()) {
            // TODO: this is probably not an exhaustive list of rhino-specific functions, but it will do for a start
            defineNonEnumerableProperty(proto, "__defineGetter__", new DefineGetter(globalObject));
            defineNonEnumerableProperty(proto, "__defineSetter__", new DefineSetter(globalObject));
            defineNonEnumerableProperty(proto, "__lookupGetter__", new LookupGetter(globalObject));
            defineNonEnumerableProperty(proto, "__lookupSetter__", new LookupSetter(globalObject));
        }
        // Support deprecated (but widely used) Object.prototype.__proto__
        defineNonEnumerableProperty(proto, "__proto__", proto);

        // Object.foo
        defineNonEnumerableProperty(this, "getPrototypeOf", new GetPrototypeOf(globalObject) );
        defineNonEnumerableProperty(this, "getOwnPropertyDescriptor", new GetOwnPropertyDescriptor(globalObject) );
        defineNonEnumerableProperty(this, "getOwnPropertyNames", new GetOwnPropertyNames(globalObject) );
        defineNonEnumerableProperty(this, "create", new Create(globalObject) );
        defineNonEnumerableProperty(this, "defineProperty", new DefineProperty(globalObject) );
        defineNonEnumerableProperty(this, "defineProperties", new DefineProperties(globalObject) );
        defineNonEnumerableProperty(this, "seal", new Seal(globalObject) );
        defineNonEnumerableProperty(this, "freeze", new Freeze(globalObject));
        defineNonEnumerableProperty(this, "preventExtensions", new PreventExtensions(globalObject));
        defineNonEnumerableProperty(this, "isSealed", new IsSealed(globalObject));
        defineNonEnumerableProperty(this, "isFrozen", new IsFrozen(globalObject));
        defineNonEnumerableProperty(this, "isExtensible", new IsExtensible(globalObject));
        defineNonEnumerableProperty(this, "keys", new Keys(globalObject));

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
        DynObject obj = (DynObject) context.construct(ctor);
        return obj;
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
