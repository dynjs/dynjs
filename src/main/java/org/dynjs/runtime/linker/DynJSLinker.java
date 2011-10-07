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
        if ("print".equals(callSiteDescriptor.getName())) {
            MethodHandle print = lookup().findStatic(RT.class, "print", methodType);
            return new GuardedInvocation(print, null);
        } else if (callSiteDescriptor.getName().startsWith("dyn:getProp")) {
            MethodType targetType = methodType(DynAtom.class, String.class);
            MethodHandle getProperty = lookup().findVirtual(Scope.class, "resolve", targetType);
            return new GuardedInvocation(getProperty, null);
        } else if (callSiteDescriptor.getName().startsWith("dynjs:bop")) {
            MethodType targetType = methodType(DynNumber.class, DynNumber.class);
            String op = linkRequest.getCallSiteDescriptor().getNameToken(2);
            MethodHandle opMH = lookup().findVirtual(DynNumber.class, op, targetType);
            MethodHandle targetHandle = linkerServices.asType(opMH, callSiteDescriptor.getMethodType());
            return new GuardedInvocation(targetHandle, null);
        } else if (callSiteDescriptor.getName().startsWith("dynjs:scope")) {
            if (callSiteDescriptor.getNameTokenCount() == 3) {
                return handleScope(callSiteDescriptor);
            }
        } else {
            String action = callSiteDescriptor.getNameToken(2);
            if (callSiteDescriptor.getName().startsWith("dynjs:compile")) {
                if (callSiteDescriptor.getNameTokenCount() == 3) {
                    MethodHandle targetHandle;
                    if ("lookup".equals(action)) {
                        MethodType type = methodType(CodeBlock.class, int.class);
                        targetHandle = lookup().findVirtual(DynThreadContext.class, "retrieve", type);
                    } else if ("function".equals(action)) {
                        MethodType targetType = methodType(Function.class, CodeBlock.class, String[].class);
                        targetHandle = lookup().findVirtual(DynJS.class, "compile", targetType);
                    } else if ("if".equals(action)) {
                        targetHandle = linkerServices.asType(RT.IF_STATEMENT, callSiteDescriptor.getMethodType());
                    } else if ("params".equals(action)) {
                        targetHandle = linkerServices.asType(RT.PARAM_POPULATOR, callSiteDescriptor.getMethodType());
                    } else {
                        throw new IllegalArgumentException("should not reach here");
                    }
                    return new GuardedInvocation(targetHandle, null);
                }
            } else if (callSiteDescriptor.getName().startsWith("dynjs:runtime")) {
                MethodHandle targetHandle;
                if ("call".equals(action)) {
                    targetHandle = linkerServices.asType(RT.FUNCTION_CALL, callSiteDescriptor.getMethodType());
                } else if ("eq".equals(action)) {
                    targetHandle = linkerServices.asType(RT.EQ, callSiteDescriptor.getMethodType());
                } else {
                    throw new IllegalArgumentException("should not reach here");
                }
                return new GuardedInvocation(targetHandle, null);
            }
        }
        return null;
    }

    private GuardedInvocation handleScope(CallSiteDescriptor callSiteDescriptor) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle targetHandle;
        String action = callSiteDescriptor.getNameToken(2);
        if ("resolve".equals(action)) {
            MethodType targetType = methodType(DynAtom.class, String.class);
            targetHandle = lookup().findVirtual(Scope.class, "resolve", targetType);
        } else if ("define".equals(action)) {
            MethodType targetType = methodType(void.class, String.class, DynAtom.class);
            targetHandle = lookup().findVirtual(Scope.class, "define", targetType);
        } else {
            throw new IllegalArgumentException("should not reach here");
        }
        return new GuardedInvocation(targetHandle, null);
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
