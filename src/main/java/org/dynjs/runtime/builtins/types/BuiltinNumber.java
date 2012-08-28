package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.NaN;
import org.dynjs.runtime.builtins.types.number.NegativeInfinity;
import org.dynjs.runtime.builtins.types.number.PositiveInfinity;
import org.dynjs.runtime.builtins.types.number.ToFixed;
import org.dynjs.runtime.builtins.types.number.ToLocaleString;
import org.dynjs.runtime.builtins.types.number.ToString;
import org.dynjs.runtime.builtins.types.number.ValueOf;

public class BuiltinNumber extends AbstractNativeFunction {

    public BuiltinNumber(final GlobalObject globalObject) {
        super(globalObject, true);

        // 15.7.4 Set the prototype
        final PrimitiveDynObject proto = new PrimitiveDynObject();
        proto.setClassName("Number");
        proto.setPrimitiveValue(0);
        proto.put( null, "constructor", this, false );
        proto.put( null, "toString", new ToString(globalObject), false );
        proto.put( null, "toLocaleString", new ToLocaleString(globalObject), false );
        proto.put( null, "valueOf", new ValueOf(globalObject), false );
        proto.put( null, "toFixed", new ToFixed(globalObject), false );
        
        put( null, "prototype", proto, false );
        setPrototype(globalObject.getPrototypeFor("Function"));

        put(null, "NaN", new NaN(globalObject), false);
        put(null, "POSITIVE_INFINITY", new PositiveInfinity(globalObject), false);
        put(null, "NEGATIVE_INFINITY", new NegativeInfinity(globalObject), false);
        globalObject.put(null, "NaN", get( null, "NaN" ), false );
        globalObject.put(null, "Infinity", get( null, "POSITIVE_INFINITY" ), false );
    }

    public static boolean isNumber(DynObject object) {
        return "Number".equals(object.getClassName());
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
    public JSObject createNewObject() {
        // 15.7.2.1
        PrimitiveDynObject object = new PrimitiveDynObject();
        object.setPrototype(this.getPrototype());
        object.setClassName("Number");
        return object;
    }

}