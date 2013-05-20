package org.dynjs.runtime.java;

public abstract class SomeParentAbstractBean {

    protected Long implementedParentGetLong() {
        return -6L;
    }
    
    public Long callImplementedParentGetLong() {
        return implementedParentGetLong();
    }
    
    protected Long someProtectedMethod() {
        return -9L;
    }
    
    protected Long someOtherProtectedMethod() {
        return -10L;
    }
}
