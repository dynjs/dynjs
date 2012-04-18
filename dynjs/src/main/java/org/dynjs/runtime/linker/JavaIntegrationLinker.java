package org.dynjs.runtime.linker;

import org.dynalang.dynalink.linker.*;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.extensions.ClassOperations;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public class JavaIntegrationLinker implements TypeBasedGuardingDynamicLinker {

    private static final Map<Class, Map<String, MethodHandle>> vtable = new HashMap<Class, Map<String, MethodHandle>>() {{
        put(Class.class, VTablePopulator.vtableFrom(ClassOperations.class));
    }};

    @Override
    public boolean canLinkType(Class<?> type) {
        return vtable.containsKey(type);
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        Object[] arguments = linkRequest.getArguments();
        Object receiver = arguments[0];
        Class<? extends Object> receiverClass = receiver.getClass();
        Map<String, MethodHandle> vtable = JavaIntegrationLinker.vtable.get(receiverClass);
        CallSiteDescriptor descriptor = linkRequest.getCallSiteDescriptor();
        MethodType targetMethodType = methodTypeForArguments(descriptor, arguments, receiverClass);

        String selector = descriptor.getName() + targetMethodType.toMethodDescriptorString();
        MethodHandle handle = vtable.get(selector);
        if (handle != null) {
            if (!descriptor.getMethodType().equals(handle.type())) {
                handle = linkerServices.asType(handle, descriptor.getMethodType());
            }
            return new GuardedInvocation(handle, Guards.isInstance(receiverClass, 0, descriptor.getMethodType()));
        }
        return null;
    }

    private MethodType methodTypeForArguments(CallSiteDescriptor descriptor, Object[] arguments, Class<? extends Object> receiverClass) {
        MethodType targetMethodType = MethodType.genericMethodType(arguments.length);
        Class<?> originalReturnType = descriptor.getMethodType().returnType();

        if (arguments.length > 1 && originalReturnType != Class.class) {
            targetMethodType = targetMethodType.changeReturnType(receiverClass);
        }

        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            targetMethodType = targetMethodType.changeParameterType(i, argument.getClass());
        }
        return targetMethodType;
    }
}
