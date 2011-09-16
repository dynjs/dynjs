package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

public class DynThreadContext {

    public DynAtom defineStringLiteral(final String value) {
        return new DynString(value);
    }

    public DynAtom defineNumberLiteral(final String value) {
        return new DynPrimitiveNumber(value);
    }

}
