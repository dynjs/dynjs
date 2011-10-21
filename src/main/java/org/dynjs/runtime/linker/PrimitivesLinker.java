/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
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
package org.dynjs.runtime.linker;

import org.dynalang.dynalink.linker.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.linker.TypeBasedGuardingDynamicLinker;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.extensions.BooleanOperations;
import org.dynjs.runtime.extensions.NumberOperations;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimitivesLinker implements TypeBasedGuardingDynamicLinker {

    private static final List<Class> TYPES = Arrays.asList(new Class[]{Double.class, Boolean.class});
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
        entryPointClassValue.get(Double.class).putAll(VTablePopulator.vtableFrom(NumberOperations.class));
        entryPointClassValue.get(Boolean.class).putAll(VTablePopulator.vtableFrom(BooleanOperations.class));
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
        MethodType targetMethodType = methodTypeForArguments(descriptor, arguments, receiverClass);

        String selector = descriptor.getName() + targetMethodType.toMethodDescriptorString();
        MethodHandle handle = vtable.get(selector);
        if (handle != null) {
            if (descriptor.getMethodType().toMethodDescriptorString() != handle.type().toMethodDescriptorString()) {
                handle = linkerServices.asType(handle, descriptor.getMethodType());
            }
            return new GuardedInvocation(handle, Guards.isInstance(receiverClass, 1, descriptor.getMethodType()));
        }
        return null;
    }

    private MethodType methodTypeForArguments(CallSiteDescriptor descriptor, Object[] arguments, Class<? extends Object> receiverClass) {
        MethodType targetMethodType = MethodType.genericMethodType(arguments.length);
        Class<?> originalReturnType = descriptor.getMethodType().returnType();
        if (originalReturnType != Object.class) {
            targetMethodType = targetMethodType.changeReturnType(originalReturnType);
        } else {
            targetMethodType = targetMethodType.changeReturnType(receiverClass);
        }
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            targetMethodType = targetMethodType.changeParameterType(i, argument.getClass());
        }
        return targetMethodType;
    }
}
