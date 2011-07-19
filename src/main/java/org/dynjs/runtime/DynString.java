package org.dynjs.runtime;

public class DynString implements DynAtom {
    private final String value;

    public DynString(String value) {
        this.value = value;
    }

    @Override
    public boolean isUndefined() {
        return false;
    }
}
