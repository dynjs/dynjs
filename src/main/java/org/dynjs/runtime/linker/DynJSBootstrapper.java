package org.dynjs.runtime.linker;

import me.qmx.jitescript.internal.org.objectweb.asm.Handle;
import me.qmx.jitescript.internal.org.objectweb.asm.Opcodes;
import org.dynjs.runtime.linker.java.array.JSJavaArrayPropertyLinker;
import org.dynjs.runtime.linker.java.clazz.JSJavaClassMethodLinker;
import org.dynjs.runtime.linker.java.clazz.JSJavaClassPropertyLinker;
import org.dynjs.runtime.linker.java.instance.JSJavaBoundMethodLinker;
import org.dynjs.runtime.linker.java.instance.JSJavaInstanceMethodLinker;
import org.dynjs.runtime.linker.java.instance.JSJavaInstancePropertyLinker;
import org.dynjs.runtime.linker.java.jsimpl.JSJavaImplementationLinker;
import org.dynjs.runtime.linker.java.jsimpl.JSJavaImplementationManager;
import org.dynjs.runtime.linker.java.map.JSMapLikePropertyLinker;
import org.dynjs.runtime.linker.js.environment.JavascriptEnvironmentLinker;
import org.dynjs.runtime.linker.js.global.GlobalLinker;
import org.dynjs.runtime.linker.js.object.JavascriptObjectLinker;
import org.dynjs.runtime.linker.js.primitive.JavascriptPrimitiveLinker;
import org.dynjs.runtime.linker.js.reference.FunctionDereferencedReferenceLinker;
import org.dynjs.runtime.linker.js.shadow.ShadowObjectLinker;
import org.dynjs.runtime.linker.js.undefined.JavascriptUndefinedLinker;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.NullLinkLogger;
import org.projectodd.rephract.RephractLinker;
import org.projectodd.rephract.java.reflect.CoercionMatrix;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import static me.qmx.jitescript.util.CodegenUtils.p;

public class DynJSBootstrapper {

    public static Handle HANDLE;
    public static Object[] ARGS = new Object[0];

    public static RephractLinker LINKER;

    private static InterpretingInvokeDynamicHandler invokeHandler;
    private static CallSiteFactory factory;

    static {
        try {
            LinkLogger logger = new NullLinkLogger();

            ShadowObjectLinker shadowLinker = new ShadowObjectLinker(logger);
            JSJavaImplementationManager implementationManager = new JSJavaImplementationManager(shadowLinker);
            CoercionMatrix coercionMatrix = new DynJSCoercionMatrix(implementationManager);
            ResolverManager manager = new ResolverManager(coercionMatrix);

            // LinkLogger logger = new FileLinkLogger("dynjs-linker.log");

            LINKER = new RephractLinker(logger);

            LINKER.addLinker(new JavascriptEnvironmentLinker(logger));

            LINKER.addLinker(new FunctionDereferencedReferenceLinker(logger));

            LINKER.addLinker(new JavascriptUndefinedLinker(logger));
            LINKER.addLinker(new JavascriptObjectLinker(logger));
            LINKER.addLinker(new JavascriptPrimitiveLinker(logger));

            LINKER.addLinker(new GlobalLinker(logger));

            LINKER.addLinker(new JSJavaBoundMethodLinker(logger, manager));
            LINKER.addLinker(new JSJavaImplementationLinker(implementationManager, logger));

            LINKER.addLinker(new JSJavaClassPropertyLinker(logger, manager));
            LINKER.addLinker(new JSJavaClassMethodLinker(logger, manager));

            LINKER.addLinker(new JSJavaArrayPropertyLinker(logger, manager));

            LINKER.addLinker(new JSJavaInstancePropertyLinker(logger, manager));
            LINKER.addLinker(new JSJavaInstanceMethodLinker(logger, manager));
            LINKER.addLinker(new JSMapLikePropertyLinker(logger, manager));

            LINKER.addLinker(shadowLinker);

            HANDLE = new Handle(Opcodes.H_INVOKESTATIC,
                    p(DynJSBootstrapper.class), "bootstrap",
                    MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class)
                            .toMethodDescriptorString());

            invokeHandler = new InterpretingInvokeDynamicHandler(LINKER);
            factory = new CallSiteFactory(LINKER);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
        return LINKER.bootstrap(lookup, name, type);
    }

    public static MethodHandle getBootstrapMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        return LINKER.getBootstrapMethodHandle();
    }

    public static InterpretingInvokeDynamicHandler getInvokeHandler() {
        return invokeHandler;
    }

    public static CallSiteFactory factory() {
        return factory;
    }

}
