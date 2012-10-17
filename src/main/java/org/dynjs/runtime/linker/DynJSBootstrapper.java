package org.dynjs.runtime.linker;

import org.dynalang.dynalink.ChainedCallSite;
import org.dynalang.dynalink.DynamicLinker;
import org.dynalang.dynalink.DynamicLinkerFactory;
import org.dynalang.dynalink.beans.BeansLinker;
import org.dynalang.dynalink.support.CallSiteDescriptorFactory;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class DynJSBootstrapper {

    public static final Handle BOOTSTRAP;
    public static final Object[] BOOTSTRAP_ARGS = new Object[0];

    private static DynamicLinker linker;

    static {
        final DynamicLinkerFactory factory = new DynamicLinkerFactory();
        factory.setPrioritizedLinkers(new DynJSLinker());
        factory.setFallbackLinkers(new BeansLinker());
        linker = factory.createLinker();
        BOOTSTRAP = new Handle(Opcodes.H_INVOKESTATIC,
                p(DynJSBootstrapper.class), "bootstrap",
                methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class)
                        .toMethodDescriptorString());
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) {
        return linker.link(new ChainedCallSite(CallSiteDescriptorFactory.create(caller, name, type)));
    }

}
