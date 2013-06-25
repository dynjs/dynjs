package org.dynjs.runtime.builtins;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.GlobalObject;
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

    public Math(final GlobalObject globalObject) {
        super(globalObject);

        // Math properties 15.8.1
        this.forceDefineReadOnlyProperty("E", java.lang.Math.E); // 15.8.1.1
        this.forceDefineReadOnlyProperty("LN10", java.lang.Math.log(10)); // 15.8.1.2
        this.forceDefineReadOnlyProperty("LN2", java.lang.Math.log(2)); // 15.8.1.3
        this.forceDefineReadOnlyProperty("LOG2E", java.lang.Math.log(java.lang.Math.E) / java.lang.Math.log(2)); // 15.8.1.4
        this.forceDefineReadOnlyProperty("LOG10E", java.lang.Math.log10(java.lang.Math.E)); // 15.8.1.5
        this.forceDefineReadOnlyProperty("PI", java.lang.Math.PI); // 15.8.1.6
        this.forceDefineReadOnlyProperty("SQRT1_2", java.lang.Math.sqrt(0.5f)); // 15.8.1.7
        this.forceDefineReadOnlyProperty("SQRT2", java.lang.Math.sqrt(2.0f)); // 15.8.1.8
        
        this.forceDefineReadOnlyProperty("NaN", Double.NaN );

        // Math functions 15.8.2
        this.forceDefineNonEnumerableProperty("abs",    new Abs(globalObject));    // 15.8.2.1
        this.forceDefineNonEnumerableProperty("acos",   new Acos(globalObject));   // 15.8.2.2
        this.forceDefineNonEnumerableProperty("asin",   new Asin(globalObject));   // 15.8.2.3
        this.forceDefineNonEnumerableProperty("atan",   new Atan(globalObject));   // 15.8.2.4
        this.forceDefineNonEnumerableProperty("atan2",  new Atan2(globalObject));  // 15.8.2.5
        this.forceDefineNonEnumerableProperty("ceil",   new Ceil(globalObject));   // 15.8.2.6
        this.forceDefineNonEnumerableProperty("cos",    new Cos(globalObject));    // 15.8.2.7
        this.forceDefineNonEnumerableProperty("exp",    new Exp(globalObject));    // 15.8.2.8
        this.forceDefineNonEnumerableProperty("floor",  new Floor(globalObject));  // 15.8.2.9
        this.forceDefineNonEnumerableProperty("log",    new Log(globalObject));    // 15.8.2.10
        this.forceDefineNonEnumerableProperty("max",    new Max(globalObject));    // 15.8.2.11
        this.forceDefineNonEnumerableProperty("min",    new Min(globalObject));    // 15.8.2.12
        this.forceDefineNonEnumerableProperty("pow",    new Pow(globalObject));    // 15.8.2.13
        this.forceDefineNonEnumerableProperty("random", new Random(globalObject)); // 15.8.2.14
        this.forceDefineNonEnumerableProperty("round",  new Round(globalObject));  // 15.8.2.15
        this.forceDefineNonEnumerableProperty("sin",    new Sin(globalObject));    // 15.8.2.16
        this.forceDefineNonEnumerableProperty("sqrt",   new Sqrt(globalObject));   // 15.8.2.17
        this.forceDefineNonEnumerableProperty("tan",    new Tan(globalObject));    // 15.8.2.18
        
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
