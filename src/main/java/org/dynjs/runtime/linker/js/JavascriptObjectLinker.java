package org.dynjs.runtime.linker.js;

import static java.lang.invoke.MethodHandles.lookup;

import java.lang.invoke.MethodHandle;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.ReferenceContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.BaseDynJSLinker;

import com.headius.invokebinder.Binder;

public class JavascriptObjectLinker extends BaseDynJSLinker {

    public static final MethodHandle SET_MUTABLE_BINDING;

    public static final MethodHandle CALL;
    public static final MethodHandle CONSTRUCT;
    public static final MethodHandle TO_OBJECT;
    

    static {
        try {

            
            SET_MUTABLE_BINDING = Binder.from(Object.class, Object.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .convert(Object.class, EnvironmentRecord.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "setMutableBinding");
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
            try {
                MethodHandle mh = Binder.from(Object.class, Object.class, ReferenceContext.class, String.class)
                        .permute(0, 1, 2, 1)
                        .convert(Object.class, EnvironmentRecord.class, ExecutionContext.class, String.class, boolean.class)
                        .filter(1, CONTEXT_FILTER)
                        .filter(3, REFERENCE_STRICTNESS_FILTER )
                        .invokeVirtual(desc.getLookup(), "getBindingValue");
                return new GuardedInvocation(mh, Guards.isInstance(EnvironmentRecord.class, mh.type()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (receiver instanceof JSObject) {
            try {
                MethodHandle mh = Binder.from(Object.class, Object.class, ReferenceContext.class, String.class)
                        .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                        .filter(1, CONTEXT_FILTER )
                        .invokeVirtual(desc.getLookup(), "get");
                return new GuardedInvocation(mh, Guards.isInstance(JSObject.class, mh.type()));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected GuardedInvocation linkSetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        final Object receiver = linkRequest.getReceiver();

        if (receiver instanceof JSObject) {
            Reference ref = ((ReferenceContext) linkRequest.getArguments()[1]).getReference();
            if (ref.isUnresolvableReference()) {
                ExecutionContext context = ((ReferenceContext) linkRequest.getArguments()[1]).getContext();
                if (ref.isStrictReference()) {
                    ThrowException ex = new ThrowException(context, context.createReferenceError(ref.getReferencedName() + " is not defined"));
                    new GuardedInvocation(getThrower(ex, desc.getMethodType()), Guards.getIdentityGuard(receiver));
                } else {
                    GlobalObject globalObj = context.getGlobalObject();
                    MethodHandle mh = Binder.from(desc.getMethodType())
                            .drop(0)
                            .insert(0, globalObj)
                            .drop(1)
                            .insert(1, context)
                            .insert(4, false)
                            .invoke(JSOBJECT_PUT);
                    return new GuardedInvocation(mh, Guards.getIdentityGuard(receiver));
                }
            } else if (ref.isPropertyReference()) {
                if (!ref.hasPrimitiveBase()) {
                    ExecutionContext context = ((ReferenceContext) linkRequest.getArguments()[1]).getContext();
                    MethodHandle mh = Binder.from(desc.getMethodType())
                            .drop(1)
                            .insert(1, context)
                            .insert(4, ref.isStrictReference())
                            .invoke(JSOBJECT_PUT);
                    return new GuardedInvocation(mh, Guards.getIdentityGuard(receiver));
                }
            }
        } else if (receiver instanceof EnvironmentRecord) {
            Reference ref = ((ReferenceContext) linkRequest.getArguments()[1]).getReference();

            ExecutionContext context = ((ReferenceContext) linkRequest.getArguments()[1]).getContext();
            Binder binder = Binder.from(desc.getMethodType())
                    .drop(1)
                    .insert(1, context)
                    .insert(4, ref.isStrictReference());
            MethodHandle mh = binder.invoke(SET_MUTABLE_BINDING);
            return new GuardedInvocation(mh, Guards.getIdentityGuard(receiver));
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
