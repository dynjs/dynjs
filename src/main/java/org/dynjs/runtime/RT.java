package org.dynjs.runtime;

import me.qmx.internal.org.objectweb.asm.Handle;
import me.qmx.internal.org.objectweb.asm.Opcodes;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static me.qmx.jitescript.util.CodegenUtils.p;

public class RT {
    public static final Handle BOOTSTRAP = new Handle(Opcodes.H_INVOKESTATIC,
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
