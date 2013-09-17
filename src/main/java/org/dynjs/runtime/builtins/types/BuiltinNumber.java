package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.dynjs.runtime.builtins.types.number.prototype.ToExponential;
import org.dynjs.runtime.builtins.types.number.prototype.ToFixed;
import org.dynjs.runtime.builtins.types.number.prototype.ToPrecision;
import org.dynjs.runtime.builtins.types.number.prototype.ToString;
import org.dynjs.runtime.builtins.types.number.prototype.ValueOf;

public class BuiltinNumber extends AbstractBuiltinType {

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject, "value");

        // 15.7.4 Set the prototype
        final PrimitiveDynObject proto = new DynNumber(globalObject, 0L);
        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));

        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToString(globalObject));
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalObject));
        defineNonEnumerableProperty(proto, "toFixed", new ToFixed(globalObject));
        defineNonEnumerableProperty(proto, "toExponential", new ToExponential(globalObject));
        defineNonEnumerableProperty(proto, "toPrecision", new ToPrecision(globalObject));

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
        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");

        if (numArgs != 0) {
            number = Types.toNumber(context, args[0]);
        }
        if (self == Types.UNDEFINED || self == Types.NULL) {
            // called as a function
            return number;
        } else {
            // called as a ctor
            PrimitiveDynObject numberObject = (PrimitiveDynObject) self;
            if (numArgs == 0) {
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

    protected static void defineReadOnlyProperty(final JSObject on, final GlobalObject globalObject, String name, final Number value) {
        PropertyDescriptor desc = new PropertyDescriptor();
        desc.set(Names.VALUE, value);
        desc.set(Names.WRITABLE, false);
        desc.set(Names.ENUMERABLE, false);
        desc.set(Names.CONFIGURABLE, false);
        on.defineOwnProperty(null, name, desc, false);
    }

    protected static void defineReadOnlyFunction(final JSObject on, final GlobalObject globalObject, String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor();
        desc.set(Names.VALUE, value);
        desc.set(Names.WRITABLE, false);
        desc.set(Names.ENUMERABLE, false);
        desc.set(Names.CONFIGURABLE, false);
        on.defineOwnProperty(null, name, desc, false);
    }

    public static Number modulo(Number a, Number b) {
        double remainder = a.doubleValue() % b.doubleValue();
        if (remainder == 0 && Double.compare(a.doubleValue(), 0.0) < 0) {
            return -0.0;
        }
        if (remainder == (long) remainder) {
            return (long) remainder;
        }
        return remainder;
    }
}
