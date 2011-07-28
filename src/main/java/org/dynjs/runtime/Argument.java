package org.dynjs.runtime;

public class Argument {
    private final String name;
    private final DynAtom value;

    public Argument(String name, DynAtom value) {
        this.name = name;
        this.value = value;
    }

    public static Argument arg(String arg, DynAtom value) {
        return new Argument(arg, value);
    }

    public DynAtom getValue() {
        return value;
    }

    public String getKey() {
        return name;
    }
}
