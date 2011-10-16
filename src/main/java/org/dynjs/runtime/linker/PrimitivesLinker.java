package org.dynjs.runtime.linker;

import org.dynalang.dynalink.linker.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.linker.TypeBasedGuardingDynamicLinker;
import org.dynalang.dynalink.support.Guards;
import org.dynalang.dynalink.support.Lookup;
import org.dynjs.runtime.RT;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimitivesLinker implements TypeBasedGuardingDynamicLinker {

    private static final List<Class> TYPES = Arrays.asList(new Class[]{Double.class});
    private static final ClassValue<Map<String, MethodHandle>> entryPointClassValue = new ClassValue<Map<String, MethodHandle>>() {
        @Override
        protected Map<String, MethodHandle> computeValue(Class<?> type) {
            return new HashMap<>();
        }
    };

    public PrimitivesLinker() {
        initVtables();
    }

    private void initVtables() {
        Map<String, MethodHandle> map = entryPointClassValue.get(Double.class);
        map.putAll(VTablePopulator.vtableFrom(RT.NumberOperations.class));
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        if (TYPES.contains(type)) {
            return true;
        }
        return false;
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        Object[] arguments = linkRequest.getArguments();
        Object receiver = arguments[0];
        Class<? extends Object> receiverClass = receiver.getClass();
        Map<String, MethodHandle> vtable = entryPointClassValue.get(receiverClass);
        CallSiteDescriptor descriptor = linkRequest.getCallSiteDescriptor();
        MethodType targetMethodType = methodTypeForArguments(arguments, receiverClass);

        String selector = descriptor.getName() + targetMethodType.toMethodDescriptorString();
        MethodHandle handle = vtable.get(selector);
        if (handle != null) {
            return new GuardedInvocation(handle, Guards.isInstance(receiverClass, 1, descriptor.getMethodType()));
        }
        return null;
    }

    private MethodType methodTypeForArguments(Object[] arguments, Class<? extends Object> receiverClass) {
        MethodType targetMethodType = MethodType.genericMethodType(arguments.length);
        targetMethodType = targetMethodType.changeReturnType(receiverClass);
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            targetMethodType = targetMethodType.changeParameterType(i, argument.getClass());
        }
        return targetMethodType;
    }
}
