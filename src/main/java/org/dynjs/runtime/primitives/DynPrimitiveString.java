package org.dynjs.runtime.primitives;

import org.dynjs.runtime.DynAtom;

public class DynPrimitiveString implements DynAtom {
    @Override
    public boolean isPrimitive() {
        return true;
    }
}
