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
}
