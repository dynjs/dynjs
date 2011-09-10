package org.dynjs.runtime;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.support.Guards;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

public class Converters {

    public static final MethodHandle DynString2String = findStatic(Converters.class, "convertDynString2String", methodType(String.class, DynString.class));
    public static final GuardedInvocation Guarded_DynString2String = new GuardedInvocation(DynString2String,
            Guards.isInstance(DynString.class, DynString2String.type()));

    public static MethodHandle findStatic(Class cls, String name, MethodType type) {
        try {
            return lookup().findStatic(cls, name, type);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
