package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMeta {

    private Class<?> clazz;
    private List<MethodHandle> constructors = new ArrayList<>();;
    private Map<String, MethodHandle> propertyGetters = new HashMap<>();;
    private Map<String, MethodMeta> methods = new HashMap<>();;

    public ClassMeta(Class<?> clazz) {
        this.clazz = clazz;
        initialize();
    }

    private void initialize() {
        Lookup lookup = MethodHandles.lookup();

        Constructor<?>[] ctors = this.clazz.getConstructors();

        for (int i = 0; i < ctors.length; ++i) {
            try {
                this.constructors.add(lookup.unreflectConstructor(ctors[i]));
            } catch (IllegalAccessException e) {
                // ignore?
            }
        }

        Method[] methods = this.clazz.getMethods();

        for (int i = 0; i < methods.length; ++i) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                String name = methods[i].getName();
                if (methods[i].getParameterTypes().length == 0) {
                    if (name.startsWith("get") && ! name.equals( "get" ) ) {
                        try {
                            this.propertyGetters.put(propertyName(name), lookup.unreflect(methods[i]));
                        } catch (IllegalAccessException e) {
                            // ignore?
                        }
                    }
                }

                try {
                    MethodMeta methodMeta = getMethodMetaFor(name);
                    methodMeta.addMethod(lookup.unreflect(methods[i]));
                } catch (IllegalAccessException e) {
                    // ignore
                }
            }
        }
    }

    protected MethodMeta getMethodMetaFor(String name) {
        MethodMeta methodMeta = this.methods.get(name);
        if (methodMeta == null) {
            methodMeta = new MethodMeta(name);
            this.methods.put(name, methodMeta);
        }
        return methodMeta;
    }

    public static String propertyName(String name) {
        return name.substring(3, 4).toLowerCase() + name.substring(4);
    }

    public MethodHandle findConstructor(Class<?>[] types) {
        for (MethodHandle each : this.constructors) {
            if (matches(each.type(), types)) {
                return each;
            }

        }
        return null;
    }

    public MethodHandle findPropertyGetter(String name) {
        return this.propertyGetters.get(name);
    }

    protected boolean matches(MethodType candidate, Class<?>[] target) {
        if (candidate.parameterCount() == target.length) {
            return true;
        }
        return false;
    }

    public Class<?> getTarget() {
        return this.clazz;
    }

    public String toString() {
        return "[ClassMeta class=" + this.clazz.getName() + "]";
    }

}
