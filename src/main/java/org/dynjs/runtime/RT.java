package org.dynjs.runtime;

import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.MethodHandle;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static org.objectweb.asm.Opcodes.MH_INVOKESTATIC;

public class RT {
    public static final MethodHandle BOOTSTRAP = new MethodHandle(MH_INVOKESTATIC,
            p(DynJSBootstrapper.class), "bootstrap", MethodType.methodType(CallSite.class,
            MethodHandles.Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
    public static final Object[] BOOTSTRAP_ARGS = new Object[0];

    /**
     * JS builtin print method
     * @param atom the
     */
    public static void print(DynAtom atom) {
        System.out.println(atom);
    }

}
