package org.dynjs.runtime;

public class PrimitiveDynObject extends DynObject {

    private Object value;
    
    protected PrimitiveDynObject(GlobalObject globalObject) {
        super( globalObject );
    }

    protected PrimitiveDynObject(GlobalObject globalObject, Object value) {
        super( globalObject );
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
        return "[" + getClass().getSimpleName() +  ": value=" + this.value + "]";
    }

}
