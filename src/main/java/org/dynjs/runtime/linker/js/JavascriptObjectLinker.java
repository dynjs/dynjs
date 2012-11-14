package org.dynjs.runtime.linker.js;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ReferenceContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.BaseDynJSLinker;

import com.headius.invokebinder.Binder;

public class JavascriptObjectLinker extends BaseDynJSLinker {

    public static final MethodHandle CALL;
    public static final MethodHandle CONSTRUCT;
    public static final MethodHandle TO_OBJECT;

    static {
        try {
            CALL = Binder.from(Object.class, Object.class, ExecutionContext.class, Object.class, Object.class, Object[].class)
                    .convert(Object.class, JSFunction.class, ExecutionContext.class, Object.class, Object.class, Object[].class)
                    .permute(1, 2, 0, 3, 4)
                    .invokeVirtual(lookup(), "call");
            CONSTRUCT = Binder.from(Object.class, Object.class, ExecutionContext.class, Object[].class)
                    .convert(Object.class, JSFunction.class, ExecutionContext.class, Object[].class)
                    .permute(1, 0, 2)
                    .invokeVirtual(lookup(), "construct");
            TO_OBJECT = Binder.from(JSObject.class, Object.class, ExecutionContext.class)
                    .permute(1, 0)
                    .invokeStatic(lookup(), Types.class, "toObject");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        GuardedInvocation result = super.getGuardedInvocation(linkRequest, linkerServices);
        final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        if (result == null) {
            if ("ToObject".equals(desc.getName())) {
                result = linkToObject(linkRequest, desc);
            }
        }
        return result;
    }

    @Override
    protected GuardedInvocation linkCall(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        if (receiver instanceof JSFunction) {
            return new GuardedInvocation(CALL, Guards.isInstance(JSFunction.class, 0, desc.getMethodType()));
        } else if (receiver instanceof JSObject) {
            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            ThrowException ex = new ThrowException(context, context.createTypeError("object not usable as a constructor"));
            return new GuardedInvocation(getThrower(ex, desc.getMethodType()), Guards.getIdentityGuard(receiver));
        }
        return null;
    }

    @Override
    protected GuardedInvocation linkNew(LinkRequest linkRequest, CallSiteDescriptor desc) {
        Object receiver = linkRequest.getReceiver();
        if (receiver instanceof JSFunction) {
            return new GuardedInvocation(CONSTRUCT, Guards.isInstance(JSFunction.class, 0, desc.getMethodType()));
        } else if (receiver instanceof JSObject) {
            ExecutionContext context = (ExecutionContext) linkRequest.getArguments()[1];
            ThrowException ex = new ThrowException(context, context.createTypeError("object not usable as a constructor"));
            return new GuardedInvocation(getThrower(ex, desc.getMethodType()), Guards.getIdentityGuard(receiver));
        }
        return null;
    }

    @Override
    protected GuardedInvocation linkGetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        final Object receiver = linkRequest.getReceiver();
        

        if (receiver instanceof EnvironmentRecord) {
            ReferenceContext context = (ReferenceContext) linkRequest.getArguments()[1];
            try {
                MethodHandle mh = Binder.from(Object.class, Object.class, ReferenceContext.class, String.class)
                        .drop(1)
                        .convert(Object.class, EnvironmentRecord.class, String.class)
                        .insert(1, context.getContext() )
                        .insert(3, context.getReference().isStrictReference() )
                        .invokeVirtual(desc.getLookup(), "getBindingValue");
                return new GuardedInvocation(mh, Guards.isInstance(EnvironmentRecord.class, mh.type()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (receiver instanceof JSObject) {
            ReferenceContext context = (ReferenceContext) linkRequest.getArguments()[1];
            try {
                MethodHandle mh = Binder.from(Object.class, Object.class, ReferenceContext.class, String.class)
                        .drop(1)
                        .convert(Object.class, JSObject.class, String.class)
                        .insert(1, context.getContext() )
                        .invokeVirtual(desc.getLookup(), "get");
                return new GuardedInvocation(mh, Guards.isInstance(JSObject.class, mh.type()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    protected GuardedInvocation linkToObject(LinkRequest linkRequest, CallSiteDescriptor callSiteDescriptor) {
        final Object receiver = linkRequest.getReceiver();
        if (receiver instanceof JSObject) {
            return new GuardedInvocation(identityMH(callSiteDescriptor), Guards.getIdentityGuard(receiver));
        }
        return new GuardedInvocation(TO_OBJECT, Guards.getIdentityGuard(receiver));
    }

    private MethodHandle identityMH(CallSiteDescriptor callSiteDescriptor) {
        return Binder.from(callSiteDescriptor.getMethodType())
                .drop(1)
                .identity();
    }
}
