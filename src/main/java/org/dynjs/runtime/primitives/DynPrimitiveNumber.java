package org.dynjs.runtime.primitives;

import org.dynjs.runtime.DynAtom;

public class DynPrimitiveNumber implements DynAtom {
    @Override
    public boolean isPrimitive() {
        return true;
    }
}
