package org.dynjs.runtime.primitives;

import org.dynjs.runtime.DynAtom;

public class DynPrimitiveNumber implements DynAtom {

    private final String value;

    public DynPrimitiveNumber(String value) {
        this.value = value;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }
}
