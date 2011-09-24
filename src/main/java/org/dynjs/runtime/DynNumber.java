package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import static java.lang.Double.*;

public class DynNumber extends DynObject {

    public static final DynNumber NAN = new DynNumber(NaN);
    private final double value;

    public DynNumber(DynPrimitiveNumber primitiveNumber) {
        this.value = primitiveNumber.getDoubleValue();
    }

    public DynNumber(double value) {
        this.value = value;
    }

    public DynNumber add(final DynNumber other) {
        return new DynNumber(this.value + other.value);
    }

    public DynNumber sub(final DynNumber other) {
        return new DynNumber(this.value - other.value);
    }

    public DynNumber mul(final DynNumber other) {
        return new DynNumber(this.value * other.value);
    }

    public static DynNumber parseInt(final DynString string) {
        return parseInt(string, new DynNumber(10));
    }

    public static DynNumber parseInt(final DynString string, final DynNumber radix) {
        if(string.equals("")){
            return NAN;
        }
        return new DynNumber(0);
    }

    public double getValue() {
        return value;
    }

    public String toString(){
        return isNaN() ? "NaN" : "" + getValue();
    }

    private boolean isNaN() {
        return Double.isNaN(getValue());
    }
}
