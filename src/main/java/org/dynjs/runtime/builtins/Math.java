package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalContext;
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

public class Math extends DynObject {

    public Math(final GlobalContext globalContext) {
        super(globalContext);

        // Math properties 15.8.1
        defineReadOnlyProperty(globalContext, "E", java.lang.Math.E); // 15.8.1.1
        defineReadOnlyProperty(globalContext, "LN10", java.lang.Math.log(10)); // 15.8.1.2
        defineReadOnlyProperty(globalContext, "LN2", java.lang.Math.log(2)); // 15.8.1.3
        defineReadOnlyProperty(globalContext, "LOG2E", java.lang.Math.log(java.lang.Math.E) / java.lang.Math.log(2)); // 15.8.1.4
        defineReadOnlyProperty(globalContext, "LOG10E", java.lang.Math.log10(java.lang.Math.E)); // 15.8.1.5
        defineReadOnlyProperty(globalContext, "PI", java.lang.Math.PI); // 15.8.1.6
        defineReadOnlyProperty(globalContext, "SQRT1_2", java.lang.Math.sqrt(0.5f)); // 15.8.1.7
        defineReadOnlyProperty(globalContext, "SQRT2", java.lang.Math.sqrt(2.0f)); // 15.8.1.8
        
        defineReadOnlyProperty(globalContext, "NaN", Double.NaN );

        // Math functions 15.8.2
        defineNonEnumerableProperty(globalContext, "abs",    new Abs(globalContext));    // 15.8.2.1
        defineNonEnumerableProperty(globalContext, "acos",   new Acos(globalContext));   // 15.8.2.2
        defineNonEnumerableProperty(globalContext, "asin",   new Asin(globalContext));   // 15.8.2.3
        defineNonEnumerableProperty(globalContext, "atan",   new Atan(globalContext));   // 15.8.2.4
        defineNonEnumerableProperty(globalContext, "atan2",  new Atan2(globalContext));  // 15.8.2.5
        defineNonEnumerableProperty(globalContext, "ceil",   new Ceil(globalContext));   // 15.8.2.6
        defineNonEnumerableProperty(globalContext, "cos",    new Cos(globalContext));    // 15.8.2.7
        defineNonEnumerableProperty(globalContext, "exp",    new Exp(globalContext));    // 15.8.2.8
        defineNonEnumerableProperty(globalContext, "floor",  new Floor(globalContext));  // 15.8.2.9
        defineNonEnumerableProperty(globalContext, "log",    new Log(globalContext));    // 15.8.2.10
        defineNonEnumerableProperty(globalContext, "max",    new Max(globalContext));    // 15.8.2.11
        defineNonEnumerableProperty(globalContext, "min",    new Min(globalContext));    // 15.8.2.12
        defineNonEnumerableProperty(globalContext, "pow",    new Pow(globalContext));    // 15.8.2.13
        defineNonEnumerableProperty(globalContext, "random", new Random(globalContext)); // 15.8.2.14
        defineNonEnumerableProperty(globalContext, "round",  new Round(globalContext));  // 15.8.2.15
        defineNonEnumerableProperty(globalContext, "sin",    new Sin(globalContext));    // 15.8.2.16
        defineNonEnumerableProperty(globalContext, "sqrt",   new Sqrt(globalContext));   // 15.8.2.17
        defineNonEnumerableProperty(globalContext, "tan",    new Tan(globalContext));    // 15.8.2.18
        
        setClassName( "Math" );
    }

    public static Object coerceLongIfPossible(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d) || (d - java.lang.Math.ceil(d) != 0) || d > Long.MAX_VALUE)
            return d;
        else
            return (long) d;
    }

}
