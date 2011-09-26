package org.dynjs.runtime;

import me.qmx.internal.org.objectweb.asm.Handle;
import me.qmx.internal.org.objectweb.asm.Opcodes;
import org.dynalang.dynalink.support.Lookup;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.dynjs.runtime.primitives.DynPrimitiveBoolean;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class RT {

    public static final Handle BOOTSTRAP = new Handle(Opcodes.H_INVOKESTATIC,
            p(DynJSBootstrapper.class), "bootstrap", methodType(CallSite.class,
            MethodHandles.Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
    public static final Object[] BOOTSTRAP_ARGS = new Object[0];

    public static final MethodHandle FUNCTION_CALL;

    public static final MethodHandle IF_STATEMENT;
    public static final MethodHandle PARAM_POPULATOR;

    static {
        MethodType functionMethodType = methodType(DynAtom.class, DynThreadContext.class, Scope.class, DynAtom[].class);
        FUNCTION_CALL = Lookup.PUBLIC.findVirtual(Function.class, "call", functionMethodType);
        MethodType ifStatementMethodType = methodType(Function.class, DynPrimitiveBoolean.class, Function.class, Function.class);
        IF_STATEMENT = Lookup.PUBLIC.findStatic(RT.class, "ifStatement", ifStatementMethodType);
        MethodType paramPouplatorMethodType = methodType(DynFunction.class, DynFunction.class, DynAtom[].class);
        PARAM_POPULATOR = Lookup.PUBLIC.findStatic(RT.class, "paramPopulator", paramPouplatorMethodType);
    }

    /**
     * JS builtin print method
     *
     * @param atom the
     */
    public static void print(DynAtom atom) {
        System.out.println(atom);
    }

    public static Function ifStatement(DynPrimitiveBoolean condition, Function target, Function fallback) {
        if (condition.getValue()) {
            return target;
        } else {
            return fallback;
        }
    }

    public static DynFunction paramPopulator(DynFunction function, DynAtom[] args) {
        String[] parameters = function.getArguments();
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];
            if (i < args.length) {
                function.define(parameter, args[i]);
            }
        }
        // function.define("arguments", args); TODO
        System.out.println("passou populator");
        return function;
    }

}
