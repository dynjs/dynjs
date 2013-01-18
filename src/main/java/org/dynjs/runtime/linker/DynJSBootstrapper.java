package org.dynjs.runtime.linker;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.dynjs.runtime.linker.java.JSJavaArrayLinkStrategy;
import org.dynjs.runtime.linker.java.JSJavaClassLinkStrategy;
import org.dynjs.runtime.linker.java.JSJavaImplementationLinkStrategy;
import org.dynjs.runtime.linker.java.JSJavaInstanceLinkStrategy;
import org.dynjs.runtime.linker.java.JavaNullReplacingLinkStrategy;
import org.dynjs.runtime.linker.js.JavascriptObjectLinkStrategy;
import org.dynjs.runtime.linker.js.JavascriptPrimitiveLinkStrategy;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.projectodd.linkfusion.FusionLinker;
import org.projectodd.linkfusion.mop.java.CoercionMatrix;
import org.projectodd.linkfusion.mop.java.ResolverManager;

public class DynJSBootstrapper {

    public static Handle HANDLE;
    public static Object[] ARGS = new Object[0];

    private static FusionLinker linker = new FusionLinker();

    private static InterpretingInvokeDynamicHandler invokeHandler;

    static {
        try {
            CoercionMatrix coercionMatrix = new CoercionMatrix();
            ResolverManager manager = new ResolverManager(coercionMatrix);

            linker.addLinkStrategy(new JavascriptObjectLinkStrategy());
            linker.addLinkStrategy(new JavascriptPrimitiveLinkStrategy());
            linker.addLinkStrategy(new JavaNullReplacingLinkStrategy());
            linker.addLinkStrategy(new JSJavaImplementationLinkStrategy());
            linker.addLinkStrategy(new JSJavaClassLinkStrategy(manager));
            linker.addLinkStrategy(new JSJavaArrayLinkStrategy());
            linker.addLinkStrategy(new JSJavaInstanceLinkStrategy(manager));

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
