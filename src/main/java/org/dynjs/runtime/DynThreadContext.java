package org.dynjs.runtime;

public class DynThreadContext {

    public DynAtom defineStringLiteral(final String value) {
        return new DynString(value);
    }

}
