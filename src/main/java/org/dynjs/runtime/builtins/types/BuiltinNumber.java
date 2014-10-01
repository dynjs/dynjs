package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.dynjs.runtime.builtins.types.number.prototype.ToExponential;
import org.dynjs.runtime.builtins.types.number.prototype.ToFixed;
import org.dynjs.runtime.builtins.types.number.prototype.ToPrecision;
import org.dynjs.runtime.builtins.types.number.prototype.ToString;
import org.dynjs.runtime.builtins.types.number.prototype.ValueOf;

public class BuiltinNumber extends AbstractBuiltinType {

    public BuiltinNumber(final GlobalContext globalContext) {
        super(globalContext, "value");

        // 15.7.4 Set the prototype
        final PrimitiveDynObject proto = new DynNumber(globalContext, 0L);
        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject proto) {
        proto.setPrototype(globalContext.getPrototypeFor("Object"));

        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext));
        defineNonEnumerableProperty(proto, "toLocaleString", new ToString(globalContext));
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalContext));
        defineNonEnumerableProperty(proto, "toFixed", new ToFixed(globalContext));
        defineNonEnumerableProperty(proto, "toExponential", new ToExponential(globalContext));
        defineNonEnumerableProperty(proto, "toPrecision", new ToPrecision(globalContext));

        defineReadOnlyProperty(this, globalContext, "NaN", Double.NaN);
        defineReadOnlyProperty(this, globalContext, "POSITIVE_INFINITY", Double.POSITIVE_INFINITY);
        defineReadOnlyProperty(this, globalContext, "NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY);
        defineReadOnlyProperty(this, globalContext, "MIN_VALUE", Double.MIN_VALUE);
        defineReadOnlyProperty(this, globalContext, "MAX_VALUE", Double.MAX_VALUE);
        defineReadOnlyProperty(globalContext.getObject(), globalContext, "NaN", Double.NaN);
        defineReadOnlyProperty(globalContext.getObject(), globalContext, "Infinity", Double.POSITIVE_INFINITY);
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
        return new DynNumber(context.getGlobalContext());
    }

    protected static void defineReadOnlyProperty(final JSObject on, final GlobalContext globalContext, String name, final Number value) {
        on.defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, false, false, false), false);
    }

    protected static void defineReadOnlyFunction(final JSObject on, final GlobalContext globalContext, String name, final Object value) {
        on.defineOwnProperty(null, name,
                PropertyDescriptor.newDataPropertyDescriptor(value, false, false, false), false);
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
