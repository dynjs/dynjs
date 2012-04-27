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

import com.headius.invokebinder.Binder;
import org.dynalang.dynalink.linker.*;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.api.Scope;
import org.dynjs.runtime.Converters;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.RT;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;

public class DynJSLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {

    public static final MethodHandle RESOLVE;
    public static final MethodHandle DEFINE;
    public static final MethodHandle GETELEMENT;
    public static final MethodHandle SETELEMENT;

    static {
        try {
            RESOLVE = Binder
                    .from(Object.class, Object.class, Object.class)
                    .convert(Object.class, Scope.class, String.class)
                    .invokeVirtual(lookup(), "resolve");
            DEFINE = Binder
                    .from(void.class, Object.class, Object.class, Object.class)
                    .convert(void.class, Scope.class, String.class, Object.class)
                    .invokeVirtual(lookup(), "define");
            GETELEMENT = Binder
                    .from(Object.class, Object.class, Object.class)
                    .filter(1, Converters.toInteger)
                    .convert(Object.class, DynArray.class, int.class)
                    .invokeVirtual(lookup(), "get");
            SETELEMENT = Binder
                    .from(void.class, Object.class, Object.class, Object.class)
                    .filter(1, Converters.toInteger)
                    .convert(void.class, DynArray.class, int.class, Object.class)
                    .invokeVirtual(lookup(), "set");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        CallSiteDescriptor callSiteDescriptor = linkRequest.getCallSiteDescriptor();
        MethodType methodType = callSiteDescriptor.getMethodType();
        MethodHandle targetHandle = null;
        if ("print".equals(callSiteDescriptor.getName())) {
            targetHandle = lookup().findStatic(RT.class, "print", methodType);
        } else if (isFromDynalink(callSiteDescriptor)) {
            if (callSiteDescriptor.getNameToken(1).equals("call")) {
                targetHandle = linkerServices.asType(RT.FUNCTION_CALL, callSiteDescriptor.getMethodType());
            } else if ("getProp".equals(callSiteDescriptor.getNameToken(1))) {
                return handleGetProp(callSiteDescriptor);
            } else if ("setProp".equals(callSiteDescriptor.getNameToken(1))) {
                return handleSetProp(callSiteDescriptor);
            } else if ("getElement".equals(callSiteDescriptor.getNameToken(1))) {
                return handleGetElement(callSiteDescriptor);
            } else if ("setElement".equals(callSiteDescriptor.getNameToken(1))) {
                return handleSetElement(callSiteDescriptor);
            }
        } else if (callSiteDescriptor.getNameTokenCount() >= 3 && callSiteDescriptor.getNameToken(0).equals("dynjs")) {
            String action = callSiteDescriptor.getNameToken(2);
            String subsystem = callSiteDescriptor.getNameToken(1);
            if (subsystem.equals("convert")) {
                switch (action) {
                    case "to_boolean":
                        targetHandle = Converters.toBoolean;
                        break;
                }
            }
        }

        if (targetHandle != null) {
            return new GuardedInvocation(targetHandle, null);
        }

        return null;
    }

    private GuardedInvocation handleGetElement(CallSiteDescriptor callSiteDescriptor) {
        return new GuardedInvocation(GETELEMENT, Guards.isInstance(Scope.class, GETELEMENT.type()));
    }

    private GuardedInvocation handleSetElement(CallSiteDescriptor callSiteDescriptor) {
        return new GuardedInvocation(SETELEMENT, Guards.isInstance(Scope.class, SETELEMENT.type()));
    }

    private GuardedInvocation handleGetProp(CallSiteDescriptor callSiteDescriptor) {
        if (hasConstantCall(callSiteDescriptor)) {
            final MethodHandle handle = Binder
                    .from(Object.class, Object.class)
                    .convert(RESOLVE.type())
                    .insert(1, callSiteDescriptor.getNameToken(2))
                    .invoke(RESOLVE);
            return new GuardedInvocation(handle,
                    Guards.isInstance(Scope.class, handle.type()));
        } else {
            return new GuardedInvocation(RESOLVE, Guards.isInstance(Scope.class, RESOLVE.type()));
        }
    }

    private GuardedInvocation handleSetProp(CallSiteDescriptor callSiteDescriptor) {
        if (hasConstantCall(callSiteDescriptor)) {
            final MethodHandle handle = Binder
                    .from(void.class, Object.class, Object.class)
                    .convert(DEFINE.type())
                    .insert(1, callSiteDescriptor.getNameToken(2))
                    .invoke(DEFINE);
            return new GuardedInvocation(handle,
                    Guards.isInstance(Scope.class, handle.type()));
        } else {
            return new GuardedInvocation(DEFINE, Guards.isInstance(Scope.class, DEFINE.type()));
        }
    }

    private boolean hasConstantCall(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() == 3;
    }

    private boolean isFromDynalink(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() > 1 && callSiteDescriptor.getNameToken(0).equals("dyn");
    }

    @Override
    public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType) {
        return null;
    }
}
