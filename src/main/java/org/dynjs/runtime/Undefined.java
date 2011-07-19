package org.dynjs.runtime;

public class Undefined implements DynAtom {

    public static final Undefined UNDEFINED = new Undefined();

    private Undefined() {
    }

    @Override
    public boolean isUndefined() {
        return true;
    }
}
