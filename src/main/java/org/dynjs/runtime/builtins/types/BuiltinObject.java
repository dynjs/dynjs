package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.object.*;
import org.dynjs.runtime.builtins.types.object.prototype.*;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.DefineGetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.DefineSetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.LookupGetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.LookupSetter;

public class BuiltinObject extends AbstractBuiltinType {

    public BuiltinObject(final GlobalContext globalContext) {
        super(globalContext, "value");

        final JSObject proto = new DynObject(globalContext);
        setPrototypeProperty( proto );
    }

    @Override
    public void initialize(GlobalContext globalContext, final JSObject proto) {
        // Object.prototype.foo()
        defineNonEnumerableProperty(proto, "constructor", this );

        defineNonEnumerableProperty(proto, "identity", new Identity(globalContext) );
        defineNonEnumerableProperty(proto, "pp", new Pp(globalContext) );
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext) );
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalContext) );
        defineNonEnumerableProperty(proto, "hasOwnProperty", new HasOwnProperty(globalContext) );
        defineNonEnumerableProperty(proto, "isPrototypeOf", new IsPrototypeOf(globalContext) );
        defineNonEnumerableProperty(proto, "propertyIsEnumerable", new PropertyIsEnumerable(globalContext) );
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalContext) );
        if (globalContext.getRuntime().getConfig().isRhinoCompatible()) {
            // TODO: this is probably not an exhaustive list of rhino-specific functions, but it will do for a start
            defineNonEnumerableProperty(proto, "__defineGetter__", new DefineGetter(globalContext));
            defineNonEnumerableProperty(proto, "__defineSetter__", new DefineSetter(globalContext));
            defineNonEnumerableProperty(proto, "__lookupGetter__", new LookupGetter(globalContext));
            defineNonEnumerableProperty(proto, "__lookupSetter__", new LookupSetter(globalContext));
        }
        // Support deprecated (but widely used) Object.prototype.__proto__
        PropertyDescriptor descriptor = new PropertyDescriptor();
        descriptor.setEnumerable(false);
        descriptor.setConfigurable(true);
        descriptor.setGetter(new Proto(globalContext));
        descriptor.setSetter(new Proto(globalContext));
        proto.defineOwnProperty(null, "__proto__", descriptor, false);

        // Object.foo
        defineNonEnumerableProperty(this, "getPrototypeOf", new GetPrototypeOf(globalContext));
        defineNonEnumerableProperty(this, "getOwnPropertyDescriptor", new GetOwnPropertyDescriptor(globalContext) );
        defineNonEnumerableProperty(this, "getOwnPropertyNames", new GetOwnPropertyNames(globalContext) );
        defineNonEnumerableProperty(this, "create", new Create(globalContext) );
        defineNonEnumerableProperty(this, "defineProperty", new DefineProperty(globalContext) );
        defineNonEnumerableProperty(this, "defineProperties", new DefineProperties(globalContext) );
        defineNonEnumerableProperty(this, "seal", new Seal(globalContext) );
        defineNonEnumerableProperty(this, "freeze", new Freeze(globalContext));
        defineNonEnumerableProperty(this, "preventExtensions", new PreventExtensions(globalContext));
        defineNonEnumerableProperty(this, "isSealed", new IsSealed(globalContext));
        defineNonEnumerableProperty(this, "isFrozen", new IsFrozen(globalContext));
        defineNonEnumerableProperty(this, "isExtensible", new IsExtensible(globalContext));
        defineNonEnumerableProperty(this, "keys", new Keys(globalContext));

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

        return new DynObject(context.getGlobalContext() );
    }

    public static DynObject newObject(ExecutionContext context) {
        BuiltinObject ctor = (BuiltinObject) context.getGlobalContext().getObject().get(context, "__Builtin_Object");
        DynObject obj = (DynObject) context.construct((Object) null, ctor);
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
