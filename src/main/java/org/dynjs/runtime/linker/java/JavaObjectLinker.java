package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.linker.BaseDynJSLinker;

import com.headius.invokebinder.Binder;

public class JavaObjectLinker extends BaseDynJSLinker {

    private MetaManager metaManager = new MetaManager();

    public JavaObjectLinker() {

    }

    @Override
    protected GuardedInvocation linkGetMethod(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        String name = (String) linkRequest.withoutRuntimeContext().getArguments()[1];

        ClassMeta meta = this.metaManager.getMetaFor(receiver.getClass());
        MethodMeta methodMeta = meta.getMethodMetaFor(name);
        if (methodMeta != null) {
            MethodHandle mh = Binder.from(desc.getMethodType()).drop(0, 3).invoke(MethodHandles.identity(MethodMeta.class).bindTo(methodMeta));

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
    protected GuardedInvocation linkCall(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();

        if (receiver instanceof Class) {

            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            if (context.getPendingConstructorCount() > 0) {

                Object[] args = (Object[]) linkRequest.withoutRuntimeContext().getArguments()[3];
                Class<?>[] spreadTypes = getSpreadTypes(args);

                MethodHandle ctor = findConstructor(desc.getMethodType(), (Class<?>) receiver, spreadTypes);
                if (ctor != null) {
                    context.decrementPendingConstructorCount();
                    return new GuardedInvocation(ctor, Guards.getIdentityGuard(receiver));
                }
            }
        } else if (receiver instanceof MethodMeta) {
            Object[] args = (Object[]) linkRequest.withoutRuntimeContext().getArguments()[3];
            Class<?>[] types = getSpreadTypes(args);
            MethodHandle meth = ((MethodMeta) receiver).findMethod(types);
            if (meth != null) {
                MethodType inbound = desc.getMethodType();
                Binder binder = Binder.from(inbound);
                binder = binder.drop(0, inbound.parameterCount() - 2);
                binder = binder.convert( meth.type().returnType(), linkRequest.withoutRuntimeContext().getArguments()[2].getClass(), Object[].class );
                binder = binder.spread(types);
                meth = binder.invoke(meth);
                return new GuardedInvocation(meth, Guards.isInstance(receiver.getClass(), inbound));
            }

        }

        return null;
    }

    @Override
    protected GuardedInvocation linkNew(LinkRequest linkRequest, CallSiteDescriptor desc) {
        ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];

        if (context.getPendingConstructorCount() > 0) {
            Object receiver = linkRequest.getReceiver();
            if (receiver instanceof Class) {

                Object[] args = (Object[]) linkRequest.withoutRuntimeContext().getArguments()[1];

                Class<?>[] spreadTypes = getSpreadTypes(args);
                MethodHandle ctor = findConstructor(desc.getMethodType(), (Class<?>) receiver, spreadTypes);

                if (ctor != null) {
                    context.decrementPendingConstructorCount();
                    return new GuardedInvocation(ctor, Guards.getIdentityGuard(receiver));
                }

            }
        }

        return null;
    }

    private Class<?>[] getSpreadTypes(Object[] args) {
        Class<?>[] spreadTypes = new Class<?>[args.length];
        for (int i = 0; i < spreadTypes.length; ++i) {
            spreadTypes[i] = args[i].getClass();
        }

        return spreadTypes;
    }

    private MethodHandle findPropertyGetter(MethodType inbound, Object receiver, String name) {
        ClassMeta meta = metaManager.getMetaFor(receiver.getClass());
        MethodHandle meth = meta.findPropertyGetter(name);
        if (meth != null) {
            Binder binder = Binder.from(inbound);
            binder = binder.drop(0, 3);
            binder = binder.insert(0, receiver);
            MethodHandle methBridge = binder.invoke(meth);
            return methBridge;
        }
        return null;
    }

    private MethodHandle findConstructor(MethodType inbound, Class<?> clazz, Class<?>[] types) {
        ClassMeta meta = metaManager.getMetaFor(clazz);
        MethodHandle ctor = meta.findConstructor(types);
        if (ctor != null) {
            Binder binder = Binder.from(inbound);
            binder = binder.drop(0, inbound.parameterCount() - 1);
            binder = binder.spread(types);
            ctor = binder.invoke(ctor);
            return ctor;
        }
        return null;
    }

}