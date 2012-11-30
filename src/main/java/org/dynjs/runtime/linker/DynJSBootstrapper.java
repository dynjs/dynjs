package org.dynjs.runtime.linker;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.dynjs.runtime.linker.java.JavaNullReplacingLinkStrategy;
import org.dynjs.runtime.linker.java.JavascriptJavaLinkStrategy;
import org.dynjs.runtime.linker.js.JavascriptObjectLinkStrategy;
import org.dynjs.runtime.linker.js.JavascriptPrimitiveLinkStrategy;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.projectodd.linkfusion.FusionLinker;

public class DynJSBootstrapper {

    public static final Handle HANDLE;
    public static final Object[] ARGS = new Object[0];

    private static FusionLinker linker = new FusionLinker();
    
    static {
        
        linker.addLinkStrategy( new JavascriptObjectLinkStrategy() );
        linker.addLinkStrategy( new JavascriptPrimitiveLinkStrategy() );
        linker.addLinkStrategy( new JavaNullReplacingLinkStrategy() );
        linker.addLinkStrategy( new JavascriptJavaLinkStrategy() );
        
        HANDLE = new Handle(Opcodes.H_INVOKESTATIC,
                p(DynJSBootstrapper.class), "bootstrap",
                MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class)
                        .toMethodDescriptorString());
    }
    
    public static CallSite bootstrap(Lookup lookup, String name, MethodType type) throws Throwable {
        return linker.bootstrap(lookup, name, type);
    }
    
    
    public static MethodHandle getBootstrapMethodHandle() throws NoSuchMethodException, IllegalAccessException {
        return linker.getBootstrapMethodHandle();
    }

}
