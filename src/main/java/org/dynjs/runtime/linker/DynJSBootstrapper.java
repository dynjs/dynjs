package org.dynjs.runtime.linker;


import org.dynalang.dynalink.DynamicLinker;
import org.dynalang.dynalink.DynamicLinkerFactory;
import org.dynalang.dynalink.MonomorphicCallSite;
import org.dynalang.dynalink.linker.GuardingDynamicLinker;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class DynJSBootstrapper {

    private static final DynamicLinker dynamicLinker;

    static {
        final DynamicLinkerFactory factory = new DynamicLinkerFactory();
        final GuardingDynamicLinker dynJsLinker = new DynJSLinker();
        factory.setPrioritizedLinker(dynJsLinker);
        dynamicLinker = factory.createLinker();
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) {
        final MonomorphicCallSite callSite = new MonomorphicCallSite(caller, name, type);
        dynamicLinker.link(callSite);
        return callSite;
    }
}
