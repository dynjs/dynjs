package org.dynjs.runtime.linker.js;

import java.lang.invoke.MethodHandle;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ReferenceContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.BaseDynJSLinker;

import com.headius.invokebinder.Binder;

public class JavascriptPrimitiveLinker extends BaseDynJSLinker {

    protected boolean isPrimitive(Object o) {
        return (o instanceof String || o instanceof Boolean || o instanceof Number);
    }

    @Override
    protected GuardedInvocation linkCall(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        if (isPrimitive(receiver) || receiver == Types.UNDEFINED || receiver == Types.NULL ) {
            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            ThrowException ex = new ThrowException(context, context.createTypeError("object not usable as a constructor"));
            return new GuardedInvocation(getThrower(ex, desc.getMethodType()), Guards.getIdentityGuard(receiver));
        }
        return null;
    }

    @Override
    protected GuardedInvocation linkNew(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        if (isPrimitive(receiver) || receiver == Types.UNDEFINED || receiver == Types.NULL ) {
            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            ThrowException ex = new ThrowException(context, context.createTypeError("object not usable as a constructor"));
            return new GuardedInvocation(getThrower(ex, desc.getMethodType()), Guards.getIdentityGuard(receiver));
        }
        return null;
    }

    protected GuardedInvocation linkGetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        final Object receiver = linkRequest.getReceiver();

        GuardedInvocation result = null;

        if ( isPrimitive(receiver ) ) {
            Object[] args = linkRequest.getArguments();
            try {
                ReferenceContext context = (ReferenceContext) linkRequest.getArguments()[1];

                MethodHandle toObj = Binder.from(JSObject.class, Object.class)
                        .insert(0, context.getContext() )
                        .invokeStatic(desc.getLookup(), Types.class, "toObject");
                MethodHandle mh = Binder.from(Object.class, Object.class, ReferenceContext.class, String.class)
                        .drop(1)
                        .insert(1, context.getContext() )
                        .filter(0, toObj)
                        .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                        .invokeVirtual(desc.getLookup(), "get");
                result = new GuardedInvocation(mh, Guards.isInstance(receiver.getClass(), mh.type()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

}
