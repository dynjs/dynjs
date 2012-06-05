/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import com.headius.invokebinder.Binder;
import org.dynjs.api.Function;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.Map;

import static java.lang.invoke.MethodType.methodType;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class RT {

    public static final Handle BOOTSTRAP = new Handle(Opcodes.H_INVOKESTATIC,
            p(DynJSBootstrapper.class), "bootstrap", methodType(CallSite.class,
            MethodHandles.Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
    public static final Handle BOOTSTRAP_2 = new Handle(Opcodes.H_INVOKESTATIC,
            p(RT.class), "bootstrap", methodType(CallSite.class,
            MethodHandles.Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
    public static final Object[] BOOTSTRAP_ARGS = new Object[0];
    public static final MethodHandle CONSTRUCT;

    static {
        try {
            CONSTRUCT = MethodHandles.lookup().findStatic(RT.class, "construct", methodType(Object.class, DynThreadContext.class, Object.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
        if ("call2".equals(name)) {
            MutableCallSite site = new MutableCallSite(methodType);
            MethodHandle target = Binder
                    .from(Object.class, Object.class, DynThreadContext.class, Object[].class)
                    .insert(0, caller)
                    .insert(1, site)
                    .invokeStatic(caller, RT.class, "callBootstrap");
            site.setTarget(target);
            return site;
        }
        return null;
    }

    public static Object callBootstrap(MethodHandles.Lookup caller, MutableCallSite site, Object self, DynThreadContext context, Object... args) throws Throwable, IllegalAccessException {
        Function f = (Function) ((DynJSCompiler.InternalDynObject) self).getProperty("call").getAttribute("value");
        return Binder.from(Object.class, Object.class, DynThreadContext.class, Object[].class)
                .convert(Object.class, f.getClass(), DynThreadContext.class, Object[].class)
                .invokeVirtual(caller, "call").invoke(f, context, args);
    }

    public static DynFunction paramPopulator(DynFunction function, Object[] args) {
        String[] parameters = function.getArguments();
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];
            if (i < args.length) {
                function.define(parameter, args[i]);
            }
        }
        return function;
    }

    public static Function callHelper(DynThreadContext context, DynFunction function, Object[] arguments) {
        Function instance = null;
        try {
            instance = (Function) function.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        instance.setContext(context);
        copyProperties(function, instance);
        paramPopulator((DynFunction) instance, arguments);
        return instance;
    }

    private static void copyProperties(DynFunction function, Function instance) {
        for (Map.Entry<String, Object> entry : function.getAllProps().entrySet()) {
            instance.define(entry.getKey(), entry.getValue());
        }
    }

    public static String typeof(Object obj) {
        if (obj == null) {
            return "object";
        } else if (obj instanceof DynObject) {
            if (((DynObject) obj).hasOwnProperty("call")) {
                return "function";
            }
            return "object";
        } else if (obj instanceof Boolean) {
            return "boolean";
        } else if (obj instanceof Number) {
            return "number";
        } else if (obj instanceof String) {
            return "string";
        }

        return "undefined";
    }

    public static Object construct(DynThreadContext context, Object obj) {
        return new DynJSCompiler.InternalDynObject(obj, null);
    }
}
