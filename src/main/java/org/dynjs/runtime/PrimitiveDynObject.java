package org.dynjs.runtime;

public class PrimitiveDynObject extends DynObject {

    private Object value;
    
    public PrimitiveDynObject() {
    }

    public PrimitiveDynObject(Object value) {
        this.value = value;
    }
    
    public void setPrimitiveValue(Object value) {
        this.value = value;
    }

    public Object getPrimitiveValue() {
        return this.value;
    }
    
    public String toString() {
        return "[PrimitiveDynObject: value=" + this.value + "]";
    }

}
