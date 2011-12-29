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
import org.dynalang.dynalink.linker.GuardingDynamicLinker;
import org.dynalang.dynalink.linker.GuardingTypeConverterFactory;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynjs.api.Scope;
import org.dynjs.runtime.Converters;
import org.dynjs.runtime.RT;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

public class DynJSLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {

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
            } else if (callSiteDescriptor.getNameToken(1).equals("getProp")) {
                targetHandle = linkerServices.asType(lookup().findVirtual(Scope.class, "resolve", MethodType.methodType(Object.class, String.class)), callSiteDescriptor.getMethodType());
            } else if (callSiteDescriptor.getNameToken(1).equals("setProp")) {
                targetHandle = linkerServices.asType(lookup().findVirtual(Scope.class, "define", MethodType.methodType(void.class, String.class, Object.class)), callSiteDescriptor.getMethodType());
            }
        } else if (callSiteDescriptor.getNameTokenCount() >= 3 && callSiteDescriptor.getNameToken(0).equals("dynjs")) {
            String action = callSiteDescriptor.getNameToken(2);
            String subsystem = callSiteDescriptor.getNameToken(1);
            if (subsystem.equals("scope")) {
                switch (action) {
                    case "resolve": {
                        targetHandle = RT.SCOPE_RESOLVE;
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("should not reach here");
                }
            } else if (subsystem.equals("convert")) {
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

    private boolean isFromDynalink(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() > 1 && callSiteDescriptor.getNameToken(0).equals("dyn");
    }

    @Override
    public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType) {

        return null;
    }
}
