package org.dynjs.runtime.linker;

import org.dynjs.runtime.linker.anno.CompanionFor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class VTablePopulator {

    public static Map<String, MethodHandle> vtableFrom(Class clazz) {
        HashMap<String, MethodHandle> map = new HashMap<String, MethodHandle>();
        if (clazz.isAnnotationPresent(CompanionFor.class)) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                try {
                    MethodHandle handle = MethodHandles.publicLookup().unreflect(method);
                    map.put(method.getName() + handle.type().toMethodDescriptorString(), handle);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return map;
    }

}
