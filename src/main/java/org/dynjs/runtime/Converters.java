package org.dynjs.runtime;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.support.Lookup;

import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodType.methodType;

public class Converters {

    public static final MethodHandle DynString2String = Lookup.PUBLIC.findStatic(Converters.class, "convertDynString2String", methodType(String.class, Object.class));
    public static final GuardedInvocation Guarded_DynString2String = new GuardedInvocation(DynString2String,
            null);

    public static String convertDynString2String(Object dynString) {
        return dynString.toString();
    }

}
