package org.dynjs.runtime.primitives;

import org.dynjs.runtime.DynAtom;

public class DynPrimitiveNull implements DynAtom {

    public static final DynPrimitiveNull NULL = new DynPrimitiveNull();

    private DynPrimitiveNull() {
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }
}
