package org.dynjs.runtime.java;

public class JavaMockery {
    
    public static int someStaticMethod() {
        return 42;
    }

    private Object value;
    private int numFields = 0;
    
    public JavaMockery() {
        this.value = "UNSET";
    }
    
    public JavaMockery(Object value) {
        this.value = value;
        this.numFields = 1;
    }
    
    public int getNumFields() {
        return this.numFields;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public Object doSomething() {
        return this.value;
    }
    
    

}
