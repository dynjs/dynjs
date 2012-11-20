package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
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
import org.dynjs.runtime.builtins.math.Max;
import org.dynjs.runtime.builtins.math.Min;
import org.dynjs.runtime.builtins.math.Pow;
import org.dynjs.runtime.builtins.math.Random;
import org.dynjs.runtime.builtins.math.Round;
import org.dynjs.runtime.builtins.math.Sin;
import org.dynjs.runtime.builtins.math.Sqrt;
import org.dynjs.runtime.builtins.math.Tan;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class Math extends DynObject {

    public Math(final GlobalObject globalObject) {
        super(globalObject);

        // Math properties 15.8.1
        defineReadOnlyProperty(globalObject, "E", java.lang.Math.E); // 15.8.1.1
        defineReadOnlyProperty(globalObject, "LN10", java.lang.Math.log(10)); // 15.8.1.2
        defineReadOnlyProperty(globalObject, "LN2", java.lang.Math.log(2)); // 15.8.1.3
        defineReadOnlyProperty(globalObject, "LOG2E", java.lang.Math.log(java.lang.Math.E) / java.lang.Math.log(2)); // 15.8.1.4
        defineReadOnlyProperty(globalObject, "LOG10E", java.lang.Math.log10(java.lang.Math.E)); // 15.8.1.5
        defineReadOnlyProperty(globalObject, "PI", java.lang.Math.PI); // 15.8.1.6
        defineReadOnlyProperty(globalObject, "SQRT1_2", java.lang.Math.sqrt(0.5f)); // 15.8.1.7
        defineReadOnlyProperty(globalObject, "SQRT2", java.lang.Math.sqrt(2.0f)); // 15.8.1.8
        
        defineReadOnlyProperty(globalObject, "NaN", Double.NaN );

        // Math functions 15.8.2
        defineNonEnumerableProperty(globalObject, "abs",    new Abs(globalObject));    // 15.8.2.1
        defineNonEnumerableProperty(globalObject, "acos",   new Acos(globalObject));   // 15.8.2.2
        defineNonEnumerableProperty(globalObject, "asin",   new Asin(globalObject));   // 15.8.2.3
        defineNonEnumerableProperty(globalObject, "atan",   new Atan(globalObject));   // 15.8.2.4
        defineNonEnumerableProperty(globalObject, "atan2",  new Atan2(globalObject));  // 15.8.2.5
        defineNonEnumerableProperty(globalObject, "ceil",   new Ceil(globalObject));   // 15.8.2.6
        defineNonEnumerableProperty(globalObject, "cos",    new Cos(globalObject));    // 15.8.2.7
        defineNonEnumerableProperty(globalObject, "exp",    new Exp(globalObject));    // 15.8.2.8
        defineNonEnumerableProperty(globalObject, "floor",  new Floor(globalObject));  // 15.8.2.9
        defineNonEnumerableProperty(globalObject, "log",    new Log(globalObject));    // 15.8.2.10
        defineNonEnumerableProperty(globalObject, "max",    new Max(globalObject));    // 15.8.2.11
        defineNonEnumerableProperty(globalObject, "min",    new Min(globalObject));    // 15.8.2.12
        defineNonEnumerableProperty(globalObject, "pow",    new Pow(globalObject));    // 15.8.2.13
        defineNonEnumerableProperty(globalObject, "random", new Random(globalObject)); // 15.8.2.14
        defineNonEnumerableProperty(globalObject, "round",  new Round(globalObject));  // 15.8.2.15
        defineNonEnumerableProperty(globalObject, "sin",    new Sin(globalObject));    // 15.8.2.16
        defineNonEnumerableProperty(globalObject, "sqrt",   new Sqrt(globalObject));   // 15.8.2.17
        defineNonEnumerableProperty(globalObject, "tan",    new Tan(globalObject));    // 15.8.2.18
        
        setClassName( "Math" );
    }

    public static Object coerceLongIfPossible(double d) {
        // TODO: I believe this is broken for doubles expressed in scientific notation
        if (Double.isInfinite(d) || Double.isNaN(d) || (d - java.lang.Math.ceil(d) != 0) || Double.MAX_VALUE == d)
            return d;
        else
            return (long) d;
    }

}
