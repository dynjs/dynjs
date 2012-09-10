package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.dynjs.runtime.builtins.types.number.prototype.ToExponential;
import org.dynjs.runtime.builtins.types.number.prototype.ToFixed;
import org.dynjs.runtime.builtins.types.number.prototype.ToString;
import org.dynjs.runtime.builtins.types.number.prototype.ValueOf;

public class BuiltinNumber extends AbstractBuiltinType {

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject, "value");

        // 15.7.4 Set the prototype
        final PrimitiveDynObject proto = new DynNumber(globalObject, 0L);
        put(null, "prototype", proto, false);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        defineReadOnlyFunction(proto, globalObject, "constructor", this);
        defineReadOnlyFunction(proto, globalObject, "toString", new ToString(globalObject));
        defineReadOnlyFunction(proto, globalObject, "toLocaleString", new ToString(globalObject));
        defineReadOnlyFunction(proto, globalObject, "valueOf", new ValueOf(globalObject));
        defineReadOnlyFunction(proto, globalObject, "toFixed", new ToFixed(globalObject));
        defineReadOnlyFunction(proto, globalObject, "toExponential", new ToExponential(globalObject));

        defineReadOnlyProperty(this, globalObject, "NaN", Double.NaN);
        defineReadOnlyProperty(this, globalObject, "POSITIVE_INFINITY", Double.POSITIVE_INFINITY);
        defineReadOnlyProperty(this, globalObject, "NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY);
        defineReadOnlyProperty(this, globalObject, "MIN_VALUE", Double.MIN_VALUE);
        defineReadOnlyProperty(this, globalObject, "MAX_VALUE", Double.MAX_VALUE);
        defineReadOnlyProperty(globalObject, globalObject, "NaN", Double.NaN);
        defineReadOnlyProperty(globalObject, globalObject, "Infinity", Double.POSITIVE_INFINITY);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Number number = 0L;
        System.err.println( "args[0] '" + args[0] + "'" );
        if (args[0] != null) {
            number = Types.toNumber(context, args[0]);
        }
        if (self == Types.UNDEFINED) {
            // called as a function
            return number;
        } else {
            // called as a ctor
            PrimitiveDynObject numberObject = (PrimitiveDynObject) self;
            if (args[0] == Types.UNDEFINED) {
                number = 0L;
            }
            numberObject.setPrimitiveValue(number);
            return numberObject;
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        // 15.7.2.1
        return new DynNumber(context.getGlobalObject());
    }

    protected static void defineReadOnlyProperty(final DynObject on, final GlobalObject globalObject, String name, final Number value) {
        on.defineOwnProperty(null, name, new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", false);
                set("Enumerable", false);
                set("Configurable", false);
            }
        }, false);
    }

    protected static void defineReadOnlyFunction(final JSObject on, final GlobalObject globalObject, String name, final Object value) {
        on.defineOwnProperty(null, name, new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", false);
                set("Enumerable", false);
                set("Configurable", false);
            }
        }, false);
    }
}