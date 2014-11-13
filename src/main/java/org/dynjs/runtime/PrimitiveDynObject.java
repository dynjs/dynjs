package org.dynjs.runtime;

public class PrimitiveDynObject extends DynObject {

    private Object value;

    protected PrimitiveDynObject(GlobalContext globalContext) {
        super(globalContext);
    }

    protected PrimitiveDynObject(GlobalContext globalContext, Object value) {
        super(globalContext);
        if (value != null) {
            setPrimitiveValue(value);
        }
    }

    public void setPrimitiveValue(Object value) {
        this.value = value;
    }

    public Object getPrimitiveValue() {
        return this.value;
    }

    public String toString() {
        return "[" + getClass().getSimpleName() + ": value=" + this.value + "]";
    }

}
