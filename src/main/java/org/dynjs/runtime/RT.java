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
import org.dynjs.api.Resolver;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.exception.ReferenceError;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

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
        } else if ("getScope".equals(name)) {
            MutableCallSite site = new MutableCallSite(methodType);
            MethodHandle getScope = Binder
                    .from(Object.class, DynThreadContext.class, Object.class, Object.class)
                    .insert(0, site)
                    .invokeStatic(caller, RT.class, "getScope");
            site.setTarget(getScope);
            return site;
        } else if ("this".equals(name)) {
            final MutableCallSite site = new MutableCallSite(methodType);
            final MethodHandle getThis = Binder
                    .from(Object.class, DynThreadContext.class, Object.class, Object.class)
                    .insert(0, caller)
                    .insert(1, site)
                    .invokeStatic(caller, RT.class, "getThis");
            site.setTarget(getThis);
            return site;
        } else if ("DefineOwnProperty".equals(name)) {
            final MutableCallSite site = new MutableCallSite(methodType);
            final MethodHandle defineOwnPropertyBootstrap = Binder
                    .from(methodType)
                    .insert(0, caller)
                    .invokeStatic(caller, RT.class, "defineOwnPropertyBootstrap");
            site.setTarget(defineOwnPropertyBootstrap);
            return site;
        }
        return null;
    }

    public static Object callBootstrap(MethodHandles.Lookup caller, MutableCallSite site, Object self, DynThreadContext context, Object... args) throws Throwable, IllegalAccessException {
        Function f = (Function) ((DynJSCompiler.InternalDynObject) self).getProperty("call").getAttribute("value");
        context.getFrameStack().push(new Frame(f, args));
        final Object result = Binder.from(Object.class, Object.class, Object.class, DynThreadContext.class, Object[].class)
                .convert(Object.class, f.getClass(), Object.class, DynThreadContext.class, Object[].class)
                .invokeVirtual(caller, "call").invoke(f, self, context, args);
        context.getFrameStack().pop();
        return result;
    }

    public static Object getScope(MutableCallSite site, final DynThreadContext context, final Object thiz, final Object self) {
        return new Resolver() {
            @Override
            public Object resolve(String name) {
                Object value = null;
                value = ((Resolver) self).resolve(name);
                if (value == null) {
                    value = ((Resolver) thiz).resolve(name);
                }
                if (value == null && thiz instanceof Function) {
                    value = context.getFrameStack().peek().resolve(name);
                }
                if (value == null) {
                    value = ((Resolver) context.getScope()).resolve(name);
                }
                if (value == null && thiz instanceof Function) {
                    value = context.getCapturedScopeStore().get(thiz.getClass()).resolve(name);
                }
                if (value == null) {
                    throw new ReferenceError(name);
                }
                return value;
            }
        };
    }

    public static Object getThis(MethodHandles.Lookup caller, MutableCallSite site, final DynThreadContext context, final Object thiz, final Object self) {
        final DynObject parent = ((DynObject) self).getParent();
        return parent;
    }

    public static void defineOwnPropertyBootstrap(MethodHandles.Lookup caller, Object self, Object propertyName, Object value) throws Throwable, IllegalAccessException {

        final MethodHandle setProperty = Binder
                .from(void.class, Object.class, Object.class, Object.class)
                .convert(void.class, self.getClass(), String.class, Object.class)
                .invokeVirtual(caller, "define");
        setProperty.invokeWithArguments(self, propertyName, value);
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

    public static Object findThis(Object thiz, Object self) {
        return thiz;
    }

    public static boolean allArgsAreSameType(Object[] args) {
        boolean isSameType = true;
        if (args.length > 0 && args[0] != null) {
            Class type = args[0].getClass();
            for (int i = 1; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null) {
                    isSameType = type.isAssignableFrom(arg.getClass());
                }
            }
        }
        return isSameType;
    }
}
