package org.dynjs;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

/**
 *
 * @author qmx
 */
public class DynJsBootstrap {

    public static CallSite bootstrap(Lookup lookup, String name, MethodType type, Object... args) throws NoSuchMethodException, IllegalAccessException {
        MutableCallSite site = new MutableCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, MutableCallSite.class);
        MethodHandle myFallback = MethodHandles.insertArguments(
                lookup.findStatic(DynJsBootstrap.class, "fallback", fallbackType),
                0,
                site);
        site.setTarget(myFallback);
        return site;
    }

    public static Object fallback(CallSite site, String name, Object... args) throws Throwable {

        MethodHandle target = null;
        if ("wtf".equals(name)) {
            target = MethodHandles.lookup().findStatic(DynJsBootstrap.class, name, site.type());
        }
        Object result = target.invokeWithArguments(args);
        return result;
    }
    public static final MethodHandle FALLBACK;

    static {
        FALLBACK = findStatic(DynJsBootstrap.class, "fallback", MethodType.methodType(CallSite.class, Object[].class));
    }

    public static MethodHandle findStatic(Class caller, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findStatic(caller, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
