package org.dynjs.runtime.java;

public abstract class SomeAbstractBean extends SomeParentAbstractBean {

    protected abstract Long abstractGetLong();

    protected Long implementedGetLong() {
        return -3L;
    }
    
    protected Long someOtherProtectedMethod() {
        return -9L;
    }
    
    public Long callAbstractGetLong() {
        return abstractGetLong();
    }
    
    public Long callImplementedGetLong() {
        return implementedGetLong();
    }
}
