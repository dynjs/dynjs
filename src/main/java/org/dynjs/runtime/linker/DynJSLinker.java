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
import org.dynjs.runtime.DynNumber;
import org.dynjs.runtime.DynString;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;

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
                        MethodType targetType = methodType(DynAtom.class, String.class);
                        targetHandle = lookup().findVirtual(Scope.class, "resolve", targetType);
                        break;
                    }
                    case "define": {
                        MethodType targetType = methodType(void.class, String.class, DynAtom.class);
                        targetHandle = lookup().findVirtual(Scope.class, "define", targetType);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("should not reach here");
                }
            } else if (subsystem.equals("compile")) {
                switch (action) {
                    case "lookup":
                        MethodType type = methodType(CodeBlock.class, int.class);
                        targetHandle = lookup().findVirtual(DynThreadContext.class, "retrieve", type);
                        break;
                    case "function":
                        MethodType targetType = methodType(Function.class, CodeBlock.class, String[].class);
                        targetHandle = lookup().findVirtual(DynJS.class, "compile", targetType);
                        break;
                    case "if":
                        targetHandle = linkerServices.asType(RT.IF_STATEMENT, callSiteDescriptor.getMethodType());
                        break;
                    case "params":
                        targetHandle = linkerServices.asType(RT.PARAM_POPULATOR, callSiteDescriptor.getMethodType());
                        break;
                    default:
                        throw new IllegalArgumentException("should not reach here");
                }
            } else if (subsystem.equals("runtime")) {
                switch (action) {
                    case "call":
                        targetHandle = linkerServices.asType(RT.FUNCTION_CALL, callSiteDescriptor.getMethodType());
                        break;
                    case "eq":
                        targetHandle = linkerServices.asType(RT.EQ, callSiteDescriptor.getMethodType());
                        break;
                    case "bop":
                        MethodType targetType = methodType(DynNumber.class, DynNumber.class);
                        String op = linkRequest.getCallSiteDescriptor().getNameToken(3);
                        MethodHandle opMH = lookup().findVirtual(DynNumber.class, op, targetType);
                        targetHandle = linkerServices.asType(opMH, callSiteDescriptor.getMethodType());
                        break;
                    default:
                        throw new IllegalArgumentException("should not reach here");
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
        if (DynString.class.isAssignableFrom(sourceType) && String.class == targetType) {
            return Converters.Guarded_DynString2String;
        } else if (DynAtom.class.isAssignableFrom(sourceType) && DynNumber.class == targetType) {
            return Converters.Guarded_DynPrimitiveNumber2DynNumber;
        } else if (DynAtom.class.isAssignableFrom(sourceType) && DynPrimitiveNumber.class == targetType) {
            return Converters.Guarded_DynNumber2DynPrimitiveNumber;
        } else if (DynAtom.class.isAssignableFrom(sourceType) && boolean.class == targetType) {
            return Converters.Guarded_DynAtom2boolean;
        }
        return null;
    }
}
