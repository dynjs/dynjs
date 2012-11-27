package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class JavaClassIntrospector {
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    private Class<?> clazz;
    private Map<String, UnboundJavaMethod> methods = new HashMap<>();

    public JavaClassIntrospector(Class<?> clazz) {
        this.clazz = clazz;
        initialize();
    }

    public Class<?> getTargetClass() {
        return this.clazz;
    }

    private void initialize() {
        Lookup lookup = MethodHandles.lookup();

        Constructor<?>[] ctors = this.clazz.getConstructors();

        for (int i = 0; i < ctors.length; ++i) {
            try {
                UnboundJavaMethod unboundCtor = getUnboundMethodFor("<init>");
                unboundCtor.add(lookup.unreflectConstructor(ctors[i]));
            } catch (IllegalAccessException e) {
                // ignore?
            }
        }

        Method[] methods = this.clazz.getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                String name = methods[i].getName();
                try {
                    UnboundJavaMethod methodMeta = getUnboundMethodFor(name);
                    methodMeta.add(lookup.unreflect(methods[i]));
                } catch (IllegalAccessException e) {
                    // ignore
                }
            }
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public MethodHandle findConstructor(Class<?>[] types) {
        UnboundJavaMethod unboundMethod = this.methods.get("<init>");
        if (unboundMethod != null) {
            return unboundMethod.findMethodHandle(types);
        }
        return null;
    }

    public MethodHandle findPropertyGetterMethodHandle(String propName) {
        UnboundJavaMethod unboundMethod = this.methods.get("get" + capitalize(propName));
        if (unboundMethod != null) {
            return unboundMethod.findMethodHandle(EMPTY_CLASS_ARRAY);
        }
        return null;
    }

    public MethodHandle findPropertySetterMethodHandle(String propName, Class<?> valueType) {
        UnboundJavaMethod unboundMethod = this.methods.get("set" + capitalize(propName));
        if (unboundMethod != null) {
            return unboundMethod.findMethodHandle(new Class<?>[] { valueType });
        }
        return null;
    }
    
    public UnboundJavaMethod findMethodFor(String name) {
        return this.methods.get(name);
    }

    protected UnboundJavaMethod getUnboundMethodFor(String name) {
        UnboundJavaMethod methodMeta = this.methods.get(name);
        if (methodMeta == null) {
            methodMeta = new UnboundJavaMethod(name);
            this.methods.put(name, methodMeta);
        }
        return methodMeta;
    }

    public String toString() {
        return "[ClassMeta class=" + this.clazz.getName() + "]";
    }

}
