package org.dynjs.runtime;

public class Attribute<T extends DynAtom> {

    private T value;

    public Attribute(T value) {
        this.value = value;
    }

    public T value() {
        return this.value;
    }

    public boolean isUndefined() {
        return this.value.isUndefined();
    }
}
