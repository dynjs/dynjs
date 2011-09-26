package org.dynjs.runtime.primitives;

import org.dynjs.runtime.DynAtom;

public class DynPrimitiveBoolean implements DynAtom {

    public static final DynPrimitiveBoolean TRUE = new DynPrimitiveBoolean(true);
    public static final DynPrimitiveBoolean FALSE = new DynPrimitiveBoolean(false);

    private final boolean value;

    private DynPrimitiveBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    public boolean getValue() {
        return value;
    }
}
