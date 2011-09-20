package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

public class DynNumber extends DynObject {

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
        return new DynNumber(0);
    }

    public double getValue() {
        return value;
    }
}
