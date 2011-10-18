package org.dynjs.runtime.extensions;

import org.dynjs.runtime.linker.anno.CompanionFor;

@CompanionFor(Double.class)
public class NumberOperations {

    public static Double add(Double a, Double b) {
        return a + b;
    }

    public static Double sub(Double a, Double b) {
        return a - b;
    }

    public static Double mul(Double a, Double b) {
        return a * b;
    }

    public static Boolean eq(Double a, Double b) {
        if (a.isNaN() || b.isNaN()) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        }
        return false;
    }

    public static Boolean gt(Double a, Double b) {
        return a > b;
    }
}
