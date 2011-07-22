package org.dynjs.runtime;

public class Argument<T> {
    private final String name;
    private final T value;

    public Argument(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public static Argument arg(String arg, DynAtom value) {
        return new Argument(arg, value);
    }
}
