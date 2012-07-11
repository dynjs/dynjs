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
package org.dynjs.runtime.linker;

import org.dynalang.dynalink.linker.*;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.extensions.BooleanOperations;
import org.dynjs.runtime.extensions.NumberOperations;
import org.dynjs.runtime.extensions.StringOperations;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;


public class PrimitivesLinker implements TypeBasedGuardingDynamicLinker {

    public static final Map<Class, Map<String, MethodHandle>> vtable = new HashMap<Class, Map<String, MethodHandle>>() {{
        put(Double.class, VTablePopulator.vtableFrom(NumberOperations.class));
        put(Boolean.class, VTablePopulator.vtableFrom(BooleanOperations.class));
        put(String.class, VTablePopulator.vtableFrom(StringOperations.class));
    }};

    @Override
    public boolean canLinkType(Class<?> type) {
        System.err.println( "canLinkType? " + type + " " + vtable.containsKey( type ));
        return vtable.containsKey(type);
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        System.err.println( "PRIMITIVES: " + linkRequest.getCallSiteDescriptor().getName() );
        Object[] arguments = linkRequest.getArguments();
        Object receiver = arguments[0];
        Class<? extends Object> receiverClass = receiver.getClass();
        System.err.println( "receiver: " + receiver );
        Map<String, MethodHandle> vtable = PrimitivesLinker.vtable.get(receiverClass);
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
        
        System.err.println( "primitivesLinker returns null" );
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
