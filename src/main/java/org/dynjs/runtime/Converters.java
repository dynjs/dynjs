package org.dynjs.runtime;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.support.Guards;
import org.dynalang.dynalink.support.Lookup;

import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodType.methodType;

public class Converters {

    public static final MethodHandle toBoolean;

    static {
        toBoolean = Lookup.PUBLIC.findStatic(Converters.class, "toBoolean", methodType(Boolean.class, Object.class));
    }

    public static final GuardedInvocation Guarded_toBoolean = new GuardedInvocation(toBoolean,
            Guards.isInstance(Object.class, methodType(Boolean.class, Object.class)));

    public static Boolean toBoolean(Object value) {
        return DynObject.toBoolean(value);
    }

}
