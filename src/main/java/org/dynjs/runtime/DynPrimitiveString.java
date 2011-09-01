package org.dynjs.runtime;

public class DynPrimitiveString implements DynAtom {
    @Override
    public boolean isPrimitive() {
        return true;
    }
}
