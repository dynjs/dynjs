package org.dynjs.runtime;

public class PrimitiveDynObject extends DynObject {

    private Object value;
    
    protected PrimitiveDynObject() {
    }

    protected PrimitiveDynObject(Object value) {
        this.value = value;
    }
    
    public void setPrimitiveValue(Object value) {
        this.value = value;
    }

    public Object getPrimitiveValue() {
        return this.value;
    }
    
    @Override
    public Object defaultValue(String hint) {
        return value;
    }

    public String toString() {
        return "[PrimitiveDynObject: value=" + this.value + "]";
    }

}
