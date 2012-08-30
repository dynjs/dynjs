package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.dynjs.runtime.builtins.types.number.prototype.ToExponential;
import org.dynjs.runtime.builtins.types.number.prototype.ToFixed;
import org.dynjs.runtime.builtins.types.number.prototype.ToLocaleString;
import org.dynjs.runtime.builtins.types.number.prototype.ToString;
import org.dynjs.runtime.builtins.types.number.prototype.ValueOf;

public class BuiltinNumber extends AbstractNativeFunction {

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject, true);

        // 15.7.4 Set the prototype
        final PrimitiveDynObject proto = new DynNumber(globalObject, 0);
        proto.put( null, "constructor", this, false );
        proto.put( null, "toString", new ToString(globalObject), false );
        proto.put( null, "toLocaleString", new ToLocaleString(globalObject), false );
        proto.put( null, "valueOf", new ValueOf(globalObject), false );
        proto.put( null, "toFixed", new ToFixed(globalObject), false );
        proto.put( null, "toExponential", new ToExponential(globalObject), false );
        
        put( null, "prototype", proto, false );
        setPrototype(globalObject.getPrototypeFor("Function"));

        put(null, "NaN", new DynNumber(globalObject, Double.NaN), false);
        put(null, "POSITIVE_INFINITY", new DynNumber(globalObject, Double.POSITIVE_INFINITY), false);
        put(null, "NEGATIVE_INFINITY", new DynNumber(globalObject, Double.NEGATIVE_INFINITY), false);
        globalObject.put(null, "NaN", get( null, "NaN" ), false );
        globalObject.put(null, "Infinity", get( null, "POSITIVE_INFINITY" ), false );
        
        ((JSObject)get( null, "NaN" )).setPrototype( proto );
        ((JSObject)get( null, "POSITIVE_INFINITY" )).setPrototype( proto );
        ((JSObject)get( null, "NEGATIVE_INFINITY" )).setPrototype( proto );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (self == Types.UNDEFINED) {
            // called as a function
            return Types.toNumber(args[0]);
        } else {
            // called as a ctor
            PrimitiveDynObject numberObject = (PrimitiveDynObject) self;
            numberObject.setPrimitiveValue(Types.toNumber(args[0]));
            return numberObject;
        }
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        // 15.7.2.1
        return new DynNumber( context.getGlobalObject() );
    }

}