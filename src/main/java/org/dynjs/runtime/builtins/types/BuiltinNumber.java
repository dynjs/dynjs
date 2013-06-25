package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
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

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject, "value");

        // 15.7.4 Set the prototype
        final PrimitiveDynObject proto = new DynNumber(globalObject, 0L);
        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));

        proto.forceDefineNonEnumerableProperty("constructor", this);
        proto.forceDefineNonEnumerableProperty("toString", new ToString(globalObject));
        proto.forceDefineNonEnumerableProperty("toLocaleString", new ToString(globalObject));
        proto.forceDefineNonEnumerableProperty("valueOf", new ValueOf(globalObject));
        proto.forceDefineNonEnumerableProperty("toFixed", new ToFixed(globalObject));
        proto.forceDefineNonEnumerableProperty("toExponential", new ToExponential(globalObject));
        proto.forceDefineNonEnumerableProperty("toPrecision", new ToPrecision(globalObject));

        this.forceDefineReadOnlyProperty("NaN", Double.NaN);
        this.forceDefineReadOnlyProperty("POSITIVE_INFINITY", Double.POSITIVE_INFINITY);
        this.forceDefineReadOnlyProperty("NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY);
        this.forceDefineReadOnlyProperty("MIN_VALUE", Double.MIN_VALUE);
        this.forceDefineReadOnlyProperty("MAX_VALUE", Double.MAX_VALUE);
        
        globalObject.forceDefineReadOnlyProperty( "NaN", Double.NaN);
        globalObject.forceDefineReadOnlyProperty( "Infinity", Double.POSITIVE_INFINITY);
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



    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinNumber.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Number>";
    }
}
