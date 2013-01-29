package org.dynjs.runtime.linker;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.dynjs.runtime.linker.java.JSJavaArrayLinkStrategy;
import org.dynjs.runtime.linker.java.JSJavaClassLinkStrategy;
import org.dynjs.runtime.linker.java.JSJavaImplementationLinkStrategy;
import org.dynjs.runtime.linker.java.JSJavaImplementationManager;
import org.dynjs.runtime.linker.java.JSJavaInstanceLinkStrategy;
import org.dynjs.runtime.linker.java.JavaNullReplacingLinkStrategy;
import org.dynjs.runtime.linker.js.JavascriptObjectLinkStrategy;
import org.dynjs.runtime.linker.js.JavascriptPrimitiveLinkStrategy;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.projectodd.linkfusion.FusionLinker;
import org.projectodd.linkfusion.LinkLogger;
import org.projectodd.linkfusion.NullLinkLogger;
import org.projectodd.linkfusion.mop.java.CoercionMatrix;
import org.projectodd.linkfusion.mop.java.ResolverManager;

public class DynJSBootstrapper {

    public static Handle HANDLE;
    public static Object[] ARGS = new Object[0];

    private static FusionLinker linker;

    private static InterpretingInvokeDynamicHandler invokeHandler;

    static {
        try {
            JSJavaImplementationManager implementationManager = new JSJavaImplementationManager();
            CoercionMatrix coercionMatrix = new DynJSCoercionMatrix( implementationManager );
            ResolverManager manager = new ResolverManager(coercionMatrix);
            
            //LinkLogger logger = new FileLinkLogger("dynjs-linker.log");
            LinkLogger logger = new NullLinkLogger();
            
            linker = new FusionLinker( logger );

            linker.addLinkStrategy(new JavascriptObjectLinkStrategy(logger));
            linker.addLinkStrategy(new JavascriptPrimitiveLinkStrategy(logger));
            linker.addLinkStrategy(new JavaNullReplacingLinkStrategy(logger));
            linker.addLinkStrategy(new JSJavaImplementationLinkStrategy(implementationManager, logger));
            linker.addLinkStrategy(new JSJavaClassLinkStrategy(logger,manager));
            linker.addLinkStrategy(new JSJavaArrayLinkStrategy(logger ));
            linker.addLinkStrategy(new JSJavaInstanceLinkStrategy(logger, manager));

            HANDLE = new Handle(Opcodes.H_INVOKESTATIC,
                    p(DynJSBootstrapper.class), "bootstrap",
                    MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class)
                            .toMethodDescriptorString());

            invokeHandler = new InterpretingInvokeDynamicHandler(linker);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
        return linker.bootstrap(lookup, name, type);
    }

    public static MethodHandle getBootstrapMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        return linker.getBootstrapMethodHandle();
    }

    public static InterpretingInvokeDynamicHandler getInvokeHandler() {
        return invokeHandler;
    }

}
