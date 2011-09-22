package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveNumber;

public class DynThreadContext {

    private ThreadLocal<DynJS> runtime = new ThreadLocal<>();

    public DynJS getRuntime() {
        return this.runtime.get();
    }

    public void setRuntime(DynJS runtime) {
        this.runtime.set(runtime);
    }

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
