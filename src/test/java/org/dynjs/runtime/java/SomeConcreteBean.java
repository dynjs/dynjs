package org.dynjs.runtime.java;

public class SomeConcreteBean extends SomeAbstractBean {
    
    @Override
    protected Long abstractGetLong() {
        return -2L;
    }
    
    @Override 
    protected Long someProtectedMethod() {
        return -7L;
    }
    
    protected Long concreteGetLong() {
        return -1L;
    }
    
    public Long callConcreteGetLong() {
        return concreteGetLong();
    }
}
