package org.dynjs.runtime.java;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class JavaClass extends DynObject {

    private Class<?> javaClass;

    public JavaClass(GlobalObject globalObject, Class<?> javaClass) {
        super(globalObject);
        this.javaClass = javaClass;
    }
    
    public Class<?> getJavaClass() {
        return this.javaClass;
    }

    @Override
    public Object get(ExecutionContext context, String name) {
        Object result = super.get(context, name);
        if (result == Types.UNDEFINED) {
            Method[] methods = this.javaClass.getMethods();
            List<Method> matchingMethods = new ArrayList<>();
            for (int i = 0; i < methods.length; ++i) {
                if (methods[i].getName().equals(name) && ((methods[i].getModifiers() & ( Modifier.PUBLIC | Modifier.STATIC ) ) != 0)) {
                    matchingMethods.add(methods[i]);
                }
            }
            if (!matchingMethods.isEmpty()) {
                result = new JavaClassMethod(this, name, matchingMethods);
            }
        }
        return result;
    }
    
    public String toString() {
        return "[JavaClass: " + this.javaClass.getName() + "]";
    }

}
