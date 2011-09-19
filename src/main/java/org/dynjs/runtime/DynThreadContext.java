package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

public class DynThreadContext {

    public DynAtom defineStringLiteral(final String value) {
        return new DynString(value);
    }

    public DynPrimitiveNumber defineDecimalLiteral(final String value) {
        return new DynPrimitiveNumber(value, 10);
    }

    public DynPrimitiveNumber defineOctalLiteral(final String value) {
        return new DynPrimitiveNumber(value, 8);
    }

}
