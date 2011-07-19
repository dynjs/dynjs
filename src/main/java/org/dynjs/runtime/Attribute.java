package org.dynjs.runtime;

public class Attribute<T> {

    private T value;
    private boolean undefined = false;

    public Attribute(T value) {
        this.value = value;
    }

    public Attribute() {
        this((T) new Undefined());
        this.undefined = true;
    }

    public T value() {
        return this.value;
    }

    public boolean isUndefined() {
        return this.undefined;
    }
}
