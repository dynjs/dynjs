package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.builtins.math.Abs;
import org.dynjs.runtime.builtins.math.Acos;
import org.dynjs.runtime.builtins.math.Asin;
import org.dynjs.runtime.builtins.math.Atan;
import org.dynjs.runtime.builtins.math.Atan2;
import org.dynjs.runtime.builtins.math.Ceil;
import org.dynjs.runtime.builtins.math.Cos;
import org.dynjs.runtime.builtins.math.Exp;
import org.dynjs.runtime.builtins.math.Floor;
import org.dynjs.runtime.builtins.math.Log;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Math extends DynObject {
    
    public Math(final GlobalObject globalObject) {
        super(globalObject);

        // Math properties 15.8.1
        defineReadOnlyProperty( globalObject, "E", java.lang.Math.E ); // 15.8.1.1
        defineReadOnlyProperty( globalObject, "LN10", java.lang.Math.log(10) ); // 15.8.1.2
        defineReadOnlyProperty( globalObject, "LN2", java.lang.Math.log(2) ); // 15.8.1.3
        defineReadOnlyProperty( globalObject, "LOG2E", java.lang.Math.log(java.lang.Math.E)/java.lang.Math.log(2) ); // 15.8.1.4
        defineReadOnlyProperty( globalObject, "LOG10E", java.lang.Math.log10(java.lang.Math.E) ); // 15.8.1.5
        defineReadOnlyProperty( globalObject, "PI", java.lang.Math.PI ); // 15.8.1.6
        defineReadOnlyProperty( globalObject, "SQRT1_2", java.lang.Math.sqrt(0.5f) ); // 15.8.1.7
        defineReadOnlyProperty( globalObject, "SQRT2", java.lang.Math.sqrt(2.0f) ); // 15.8.1.8
        
        // Math functions 15.8.2
        put(null, "abs",   new Abs(globalObject), false);   // 15.8.2.1
        put(null, "acos",  new Acos(globalObject), false);  // 15.8.2.2
        put(null, "asin",  new Asin(globalObject), false);  // 15.8.2.3
        put(null, "atan",  new Atan(globalObject), false);  // 15.8.2.4
        put(null, "atan2", new Atan2(globalObject), false); // 15.8.2.5
        put(null, "ceil",  new Ceil(globalObject), false);  // 15.8.2.6
        put(null, "cos",   new Cos(globalObject), false);   // 15.8.2.7
        put(null, "exp",   new Exp(globalObject), false);   // 15.8.2.8
        put(null, "floor", new Floor(globalObject), false); // 15.8.2.9
        put(null, "log",   new Log(globalObject), false);   // 15.8.2.10
    }

    protected void defineReadOnlyProperty(final GlobalObject globalObject, String name, final Number value) {
        this.defineOwnProperty( null, name, new PropertyDescriptor() {
            {
                set("Value", new DynNumber(globalObject, value));
                set("Writable", false);
                set("Enumerable", false);
                set("Configurable", false);
            }
        }, false );
    }


}
