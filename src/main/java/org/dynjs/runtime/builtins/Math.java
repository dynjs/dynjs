package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.builtins.math.Abs;
import org.dynjs.runtime.builtins.math.Acos;
import org.dynjs.runtime.builtins.math.Asin;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Math extends DynObject {
    
    public Math(GlobalObject globalObject) {
        super(globalObject);

        // Math properties 15.8.1
        put(null, "E", new DynNumber(globalObject, java.lang.Math.E), false); // 15.8.1.1
        put(null, "LN10", new DynNumber(globalObject, java.lang.Math.log(10)), false); // 15.8.1.2
        put(null, "LN2", new DynNumber(globalObject, java.lang.Math.log(2)), false); // 15.8.1.3
        put(null, "LOG2E", new DynNumber(globalObject, java.lang.Math.log(java.lang.Math.E)/java.lang.Math.log(2)), false); // 15.8.1.4
        put(null, "LOG10E", new DynNumber(globalObject, java.lang.Math.log10(java.lang.Math.E)), false);  // 15.8.1.5
        put(null, "PI", new DynNumber(globalObject, java.lang.Math.PI), false);  // 15.8.1.6
        put(null, "SQRT1_2", new DynNumber(globalObject, java.lang.Math.sqrt(0.5f)), false);  // 15.8.1.7
        put(null, "SQRT2", new DynNumber(globalObject, java.lang.Math.sqrt(2.0f)), false);  // 15.8.1.8
        
        // Math functions 15.8.2
        put(null, "abs", new Abs(globalObject), false); // 15.8.2.1
        put(null, "acos", new Acos(globalObject), false); // 15.8.2.2
        put(null, "asin", new Asin(globalObject), false); // 15.8.2.3
    }


}
