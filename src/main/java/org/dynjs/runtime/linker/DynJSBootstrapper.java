package org.dynjs.runtime.linker;

import static java.lang.invoke.MethodType.methodType;
import static me.qmx.jitescript.util.CodegenUtils.p;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynalang.dynalink.ChainedCallSite;
import org.dynalang.dynalink.DynamicLinker;
import org.dynalang.dynalink.DynamicLinkerFactory;
import org.dynalang.dynalink.support.CallSiteDescriptorFactory;
import org.dynjs.runtime.linker.js.JavascriptObjectLinker;
import org.dynjs.runtime.linker.js.JavascriptPrimitiveLinker;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

public class DynJSBootstrapper {

    public static final Handle BOOTSTRAP;
    public static final Object[] BOOTSTRAP_ARGS = new Object[0];

    private static DynamicLinker linker;

    static {
        final DynamicLinkerFactory factory = new DynamicLinkerFactory();
        factory.setPrioritizedLinkers(new JavascriptObjectLinker(), new JavascriptPrimitiveLinker(), new JavascriptObjectLinker() );
        factory.setFallbackLinkers(new NoOpLinker() );
        //factory.setFallbackLinkers(new BeansLinker());
        factory.setRuntimeContextArgCount(1);
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
