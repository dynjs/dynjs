package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.linker.BaseDynJSLinker;
import org.dynjs.runtime.linker.Filters;

import com.headius.invokebinder.Binder;

public class JavaObjectLinker extends BaseDynJSLinker {

    private Map<Class<?>, JavaClassIntrospector> introspectors = new WeakHashMap<>();

    public JavaObjectLinker() {

    }

    @Override
    protected GuardedInvocation linkGetMethod(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        String name = (String) linkRequest.withoutRuntimeContext().getArguments()[1];

        JavaClassIntrospector introspector = getClassIntrospector(receiver.getClass());
        UnboundJavaMethod unboundMethod = introspector.findMethodFor(name);
        if (unboundMethod != null) {
            MethodHandle mh = Binder.from(desc.getMethodType()).drop(0, 3).invoke(MethodHandles.identity(UnboundJavaMethod.class).bindTo(unboundMethod));

            return new GuardedInvocation(mh, Guards.isInstance(receiver.getClass(), 0, mh.type()));
        }

        return null;
    }

    @Override
    protected GuardedInvocation linkGetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        String name = (String) linkRequest.withoutRuntimeContext().getArguments()[1];

        MethodHandle meth = findPropertyGetter(desc.getMethodType(), receiver, name);

        if (meth != null) {
            return new GuardedInvocation(meth, Guards.isInstance(receiver.getClass(), 0, meth.type()));
        }
        return null;
    }

    @Override
    protected GuardedInvocation linkSetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        String name = (String) linkRequest.withoutRuntimeContext().getArguments()[1];

        Object value = linkRequest.withoutRuntimeContext().getArguments()[2];
        MethodHandle meth = findPropertySetter(desc.getMethodType(), receiver, name, value.getClass());

        if (meth != null) {
            return new GuardedInvocation(meth, Guards.isInstance(receiver.getClass(), 0, meth.type()));
        }

        return null;
    }

    @Override
    protected GuardedInvocation linkCall(LinkRequest linkRequest, CallSiteDescriptor desc) {
        System.err.println( "LINK CALL: " + linkRequest );
        Object receiver = linkRequest.getReceiver();

        if (receiver instanceof Class) {
            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            if (context.getPendingConstructorCount() > 0) {

                Object[] args = (Object[]) linkRequest.withoutRuntimeContext().getArguments()[3];
                Class<?>[] spreadTypes = getSpreadTypes(context, args);

                MethodHandle ctor = findConstructor(desc.getMethodType(), (Class<?>) receiver, spreadTypes, context);
                if (ctor != null) {
                    System.err.println("NEW!");
                    context.decrementPendingConstructorCount();
                    linkRequest.replaceArguments(desc, toJavaArguments(context, linkRequest));
                    return new GuardedInvocation(ctor, Guards.getIdentityGuard(receiver));
                }
            }
        } else if (receiver instanceof UnboundJavaMethod) {
            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            Object[] args = (Object[]) linkRequest.withoutRuntimeContext().getArguments()[3];
            Class<?>[] types = getSpreadTypes(context, args);
            MethodHandle meth = ((UnboundJavaMethod) receiver).findMethodHandle(types);
            if (meth != null) {
                MethodType inbound = desc.getMethodType();
                Binder binder = Binder.from(inbound)
                        .printType()
                        .filterReturn(RETURN_FILTER)
                        .printType()
                        .drop(0, inbound.parameterCount() - 2)
                        .printType()
                        .convert(Object.class, linkRequest.withoutRuntimeContext().getArguments()[2].getClass(), Object[].class)
                        .printType()
                        .spread(types)
                        .printType();
                meth = binder.invoke(meth);
                return new GuardedInvocation(meth, Guards.isInstance(receiver.getClass(), inbound));
            }

        }

        return null;
    }

    @Override
    protected GuardedInvocation linkNew(LinkRequest linkRequest, CallSiteDescriptor desc) {
        System.err.println( "LINK NEW: " + linkRequest );
        ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];

        if (context.getPendingConstructorCount() > 0) {
            Object receiver = linkRequest.getReceiver();
            if (receiver instanceof Class) {

                Object[] args = (Object[]) linkRequest.withoutRuntimeContext().getArguments()[1];

                Class<?>[] spreadTypes = getSpreadTypes(context, args);
                MethodHandle ctor = findConstructor(desc.getMethodType(), (Class<?>) receiver, spreadTypes, context);

                System.err.println("NEW!");
                if (ctor != null) {
                    System.err.println("IN ARGS: " + Arrays.asList(linkRequest.getArguments()));
                    linkRequest.replaceArguments(desc, toJavaArguments(context, linkRequest));
                    System.err.println("OT ARGS: " + Arrays.asList(linkRequest.getArguments()));
                    context.decrementPendingConstructorCount();
                    return new GuardedInvocation(ctor, Guards.getIdentityGuard(receiver));
                }
            }
        }

        return null;
    }

    private Object[] toJavaArguments(ExecutionContext context, LinkRequest linkRequest) {
        Object[] requestArgs = linkRequest.getArguments();
        Object[] methodArgs = (Object[]) requestArgs[ requestArgs.length - 1 ];
        Object[] javaArgs = new Object[methodArgs.length];
        for (int i = 0; i < methodArgs.length; ++i) {
            javaArgs[i] = Filters.filterToJava(context, methodArgs[i]);
        }

        requestArgs[ requestArgs.length - 1 ] = javaArgs;
        return requestArgs;
    }

    private Class<?>[] getSpreadTypes(ExecutionContext context, Object[] args) {
        Class<?>[] spreadTypes = new Class<?>[args.length];
        for (int i = 0; i < spreadTypes.length; ++i) {
            spreadTypes[i] = args[i].getClass();
        }

        return spreadTypes;
    }

    private MethodHandle findPropertyGetter(MethodType inbound, Object receiver, String name) {
        JavaClassIntrospector introspector = getClassIntrospector(receiver.getClass());
        MethodHandle meth = introspector.findPropertyGetterMethodHandle(name);
        if (meth != null) {
            Binder binder = Binder.from(inbound);
            binder = binder.drop(0, 3);
            binder = binder.insert(0, receiver);
            MethodHandle methBridge = binder.invoke(meth);
            return methBridge;
        }
        return null;
    }

    private MethodHandle findPropertySetter(MethodType inbound, Object receiver, String name, Class<?> type) {
        JavaClassIntrospector introspector = getClassIntrospector(receiver.getClass());
        MethodHandle meth = introspector.findPropertySetterMethodHandle(name, type);
        if (meth != null) {
            Binder binder = Binder.from(inbound);
            binder = binder.drop(0, 3);
            binder = binder.insert(0, receiver);
            MethodHandle methBridge = binder.invoke(meth);
            return methBridge;
        }
        return null;
    }

    private MethodHandle findConstructor(MethodType inbound, Class<?> clazz, Class<?>[] types, ExecutionContext context) {
        JavaClassIntrospector introspector = getClassIntrospector(clazz);
        MethodHandle ctor = introspector.findConstructor(types);
        if (ctor != null) {
            Binder binder = Binder.from(inbound)
                    .drop(0, inbound.parameterCount() - 1)
                    .spread(types);
            ctor = binder.invoke(ctor);
            return ctor;
        }
        return null;
    }

    private JavaClassIntrospector getClassIntrospector(Class<?> type) {
        JavaClassIntrospector introspector = this.introspectors.get(type);
        if (introspector == null) {
            introspector = new JavaClassIntrospector(type);
            this.introspectors.put(type, introspector);
        }
        return introspector;
    }

}