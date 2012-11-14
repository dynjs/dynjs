package org.dynjs.runtime.java;

import java.lang.reflect.Method;
import java.util.List;

public class JavaClassMethod {

    private JavaClass javaClass;
    private String name;
    private List<Method> methods;

    public JavaClassMethod(JavaClass javaClass, String name, List<Method> methods) {
        this.javaClass = javaClass;
        this.name = name;
        this.methods = methods;
    }

    public JavaClass getJavaClass() {
        return this.javaClass;
    }

    public List<Method> getMethods() {
        return this.methods;
    }
    
    public String getName() {
        return this.name;
    }

    public String toString() {
        return "[JavaClassMethods methods=" + this.methods + "]";
    }

}
