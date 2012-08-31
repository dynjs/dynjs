package org.dynjs.runtime.builtins.types;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.types.math.Abs;
import org.dynjs.runtime.builtins.types.math.Acos;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class BuiltinMath extends AbstractNativeFunction {
    
    public BuiltinMath(GlobalObject globalObject) {
        super(globalObject);
        setPrototype(globalObject.getPrototypeFor("Object"));

        // Math properties 15.8.1
        put(null, "E", new DynNumber(globalObject, Math.E), false); // 15.8.1.1
        put(null, "LN10", new DynNumber(globalObject, Math.log(10)), false); // 15.8.1.2
        put(null, "LN2", new DynNumber(globalObject, Math.log(2)), false); // 15.8.1.3
        put(null, "LOG2E", new DynNumber(globalObject, Math.log(Math.E)/Math.log(2)), false); // 15.8.1.4
        put(null, "LOG10E", new DynNumber(globalObject, Math.log10(Math.E)), false);  // 15.8.1.5
        put(null, "PI", new DynNumber(globalObject, Math.PI), false);  // 15.8.1.6
        put(null, "SQRT1_2", new DynNumber(globalObject, Math.sqrt(0.5f)), false);  // 15.8.1.7
        put(null, "SQRT2", new DynNumber(globalObject, Math.sqrt(2.0f)), false);  // 15.8.1.8
        
        // Math functions 15.8.2
        put(null, "abs", new Abs(globalObject), false); // 15.8.2.1
        put(null, "acos", new Acos(globalObject), false); // 15.8.2.2
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        throw new ThrowException(context.createTypeError("Math is not a function"));
    }

}
