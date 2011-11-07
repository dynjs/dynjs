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

import me.qmx.jitescript.CodeBlock;
import org.dynalang.dynalink.linker.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.GuardingDynamicLinker;
import org.dynalang.dynalink.linker.GuardingTypeConverterFactory;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.Converters;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

public class DynJSLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        CallSiteDescriptor callSiteDescriptor = linkRequest.getCallSiteDescriptor();
        MethodType methodType = callSiteDescriptor.getMethodType();
        MethodHandle targetHandle = null;
        if ("print".equals(callSiteDescriptor.getName())) {
            targetHandle = lookup().findStatic(RT.class, "print", methodType);
        } else if (callSiteDescriptor.getName().startsWith("dyn:getProp")) {
            MethodType targetType = methodType(DynAtom.class, String.class);
            targetHandle = lookup().findVirtual(Scope.class, "resolve", targetType);
        } else if (callSiteDescriptor.getNameTokenCount() >= 3 && callSiteDescriptor.getNameToken(0).equals("dynjs")) {
            String action = callSiteDescriptor.getNameToken(2);
            String subsystem = callSiteDescriptor.getNameToken(1);
            if (subsystem.equals("scope")) {
                switch (action) {
                    case "resolve": {
                        targetHandle = RT.SCOPE_RESOLVE;
                        break;
                    }
                    case "define": {
                        MethodType targetType = methodType(void.class, String.class, Object.class);
                        targetHandle = lookup().findVirtual(Scope.class, "define", targetType);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("should not reach here");
                }
            } else if (subsystem.equals("compile")) {
                switch (action) {
                    case "function":
                        MethodType targetType = methodType(Function.class, CodeBlock.class, String[].class);
                        targetHandle = lookup().findVirtual(DynJS.class, "compile", targetType);
                        break;
                    default:
                        throw new IllegalArgumentException("should not reach here");
                }
            } else if (subsystem.equals("runtime")) {
                switch (action) {
                    case "call":
                        targetHandle = linkerServices.asType(RT.FUNCTION_CALL, callSiteDescriptor.getMethodType());
                        break;
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

    @Override
    public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType) {

        return null;
    }
}
