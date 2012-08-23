package org.dynjs.exception;

public class ThrowException extends DynJSException {
    
    private Object value;

    public ThrowException(Object value) {
        this.value = value;
    }
    
    public Object getValue() {
        return this.value;
    }

}
