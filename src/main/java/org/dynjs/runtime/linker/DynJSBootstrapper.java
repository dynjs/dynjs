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

    private static RephractLinker linker;

    private static InterpretingInvokeDynamicHandler invokeHandler;

    static {
        try {
            LinkLogger logger = new NullLinkLogger();

            ShadowObjectLinker shadowLinker = new ShadowObjectLinker(logger);
            JSJavaImplementationManager implementationManager = new JSJavaImplementationManager(shadowLinker);
            CoercionMatrix coercionMatrix = new DynJSCoercionMatrix(implementationManager);
            ResolverManager manager = new ResolverManager(coercionMatrix);

            // LinkLogger logger = new FileLinkLogger("dynjs-linker.log");

            linker = new RephractLinker(logger);

            linker.addLinker(new JavascriptEnvironmentLinker(logger));

            linker.addLinker(new FunctionDereferencedReferenceLinker(logger));

            linker.addLinker(new JavascriptUndefinedLinker(logger));
            linker.addLinker(new JavascriptObjectLinker(logger));
            linker.addLinker(new JavascriptPrimitiveLinker(logger));

            linker.addLinker(new GlobalLinker(logger));

            linker.addLinker(new JSJavaBoundMethodLinker(logger, manager));
            linker.addLinker(new JSJavaImplementationLinker(implementationManager, logger));

            linker.addLinker(new JSJavaClassPropertyLinker(logger, manager));
            linker.addLinker(new JSJavaClassMethodLinker(logger, manager));

            linker.addLinker(new JSJavaArrayPropertyLinker(logger, manager));

            linker.addLinker(new JSJavaInstancePropertyLinker(logger, manager));
            linker.addLinker(new JSJavaInstanceMethodLinker(logger, manager));
            linker.addLinker(new JSMapLikePropertyLinker(logger, manager));

            linker.addLinker(shadowLinker);

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
