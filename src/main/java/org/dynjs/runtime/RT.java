package org.dynjs.runtime;

import me.qmx.internal.org.objectweb.asm.Handle;
import me.qmx.internal.org.objectweb.asm.Opcodes;
import org.dynalang.dynalink.support.Lookup;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.exception.ReferenceError;
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
    public static final MethodHandle EQ;
    public static final MethodHandle SCOPE_RESOLVE;

    static {
        MethodType functionMethodType = methodType(DynAtom.class, DynThreadContext.class, Scope.class, DynAtom[].class);
        FUNCTION_CALL = Lookup.PUBLIC.findVirtual(Function.class, "call", functionMethodType);
        MethodType ifStatementMethodType = methodType(Function.class, DynPrimitiveBoolean.class, Function.class, Function.class);
        IF_STATEMENT = Lookup.PUBLIC.findStatic(RT.class, "ifStatement", ifStatementMethodType);
        MethodType eqMethodType = methodType(DynPrimitiveBoolean.class, DynAtom.class, DynAtom.class);
        EQ = Lookup.PUBLIC.findStatic(DynObject.class, "eq", eqMethodType);
        MethodType scopeResolveMethodType = methodType(DynAtom.class, DynThreadContext.class, Scope.class, String.class);
        SCOPE_RESOLVE = Lookup.PUBLIC.findStatic(RT.class, "scopeResolve", scopeResolveMethodType);
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
        return function;
    }

    public static DynAtom scopeResolve(DynThreadContext context, Scope scope, String id) {
        DynAtom atom = scope.resolve(id);
        if (atom == null) {
            for (Function callee : context.getCallStack()) {
                atom = callee.resolve(id);
                if (atom != null) {
                    break;
                }
            }
        }
        if (atom == null) {
            atom = context.getScope().resolve(id);
        }
        if (atom == null) {
            throw new ReferenceError();
        }
        return atom;
    }

}
