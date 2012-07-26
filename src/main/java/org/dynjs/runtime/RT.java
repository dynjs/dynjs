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
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.exception.DynJSException;
import org.dynjs.exception.ReferenceError;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
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
    public static final MethodType FCALL_MT = MethodType.methodType(Object.class, Function.class, Object.class, DynThreadContext.class, Object[].class);
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
        } else if ("throw".equals(name)) {
            final ConstantCallSite throwException = new ConstantCallSite(Binder
                    .from(methodType)
                    .invokeStatic(caller, RT.class, "throwException"));
            return throwException;
        } else if ("trycatchfinally".equals(name)) {
            final ConstantCallSite throwException = new ConstantCallSite(Binder
                    .from(methodType)
                    .insert(0, caller)
                    .invokeStatic(caller, RT.class, "trycatchfinally"));
            return throwException;
        } else if ("setProp".equals(name)) {
            return new ConstantCallSite(Binder
                    .from(methodType)
                    .insert(0, caller)
                    .invokeStatic(caller, RT.class, "setProperty")
            );
        }
        return null;
    }

    public static Object setProperty(MethodHandles.Lookup caller, Object scope, Object target, Object name, Object value) {
        if (target instanceof DynThreadContext.Undefined) {
            if (scope instanceof DelegatingScopeResolver) {
                extractSelf((DelegatingScopeResolver) scope).define((String) name, value);
            } else if (scope instanceof Scope) {
                ((Scope) scope).define((String) name, value);
            }
        }
        return value;
    }

    private static Scope extractSelf(DelegatingScopeResolver scope) {
        return (Scope) ((DelegatingScopeResolver) scope).self;
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
        return new DelegatingScopeResolver(context, thiz, self);
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

    public static void throwException(Object o) {
        throw new DynJSException(String.valueOf(o));
    }

    public static void trycatchfinally(MethodHandles.Lookup caller, Object self, DynThreadContext context, Object _try, Object _catch, Object _finally) throws Throwable, IllegalAccessException {
        final MethodHandle pushException = Binder
                .from(void.class, Object.class, Throwable.class)
                .convert(void.class, Function.class, Throwable.class)
                .insert(0, context)
                .invokeStatic(caller, RT.class, "pushException");

        final MethodHandle catchHandle = Binder.from(Object.class, Object.class, Throwable.class)
                .fold(pushException)
                .convert(void.class, Function.class, Object.class)
                .collect(1, Object[].class)
                .insert(1, self)
                .insert(2, context)
                .convert(FCALL_MT)
                .invokeVirtual(caller, "call")
                .bindTo(_catch);

        final MethodHandle tryHandle = Binder.from(Object.class, Object.class)
                .convert(void.class, Function.class)
                .collect(1, Object[].class)
                .insert(1, self)
                .insert(2, context)
                .convert(FCALL_MT)
                .catchException(Throwable.class, catchHandle)
                .invokeVirtual(caller, "call")
                .bindTo(_try);

        final MethodHandle finallyHandle = Binder.from(Object.class, Object.class)
                .convert(void.class, Function.class)
                .collect(1, Object[].class)
                .insert(1, self)
                .insert(2, context)
                .convert(FCALL_MT)
                .invokeVirtual(caller, "call")
                .bindTo(_finally);

        try {
            tryHandle.invoke();
        } finally {
            finallyHandle.invoke();
        }

        // FIXME: REFACTOR ME PLEASE
        if (!context.getFrameStack().isEmpty()) {
            context.getFrameStack().pop();
        }
    }

    public static void pushException(DynThreadContext context, Function f, Throwable t) {
        context.getFrameStack().push(new Frame(f, t));
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

    private static class DelegatingScopeResolver implements Resolver {
        private final DynThreadContext context;
        private final Object thiz;
        private final Object self;

        public DelegatingScopeResolver(DynThreadContext context, Object thiz, Object self) {
            this.context = context;
            this.thiz = thiz;
            this.self = self;
        }

        @Override
        public Object resolve(String name) {
            Object value = null;
            value = ((Resolver) self).resolve(name);
            if (value == null) {
                value = ((Resolver) thiz).resolve(name);
            }
            if (value == null && thiz instanceof Function) {
                final Frame frame = context.getFrameStack().peek();
                if (frame != null) {
                    value = frame.resolve(name);
                }
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
    }
}
