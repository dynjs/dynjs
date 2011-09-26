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
                if ("resolve".equals(callSiteDescriptor.getNameToken(2))) {
                    MethodType targetType = methodType(DynAtom.class, String.class);
                    MethodHandle getProperty = lookup().findVirtual(Scope.class, "resolve", targetType);
                    return new GuardedInvocation(getProperty, null);
                } else if ("define".equals(callSiteDescriptor.getNameToken(2))) {
                    MethodType targetType = methodType(void.class, String.class, DynAtom.class);
                    MethodHandle setProperty = lookup().findVirtual(Scope.class, "define", targetType);
                    return new GuardedInvocation(setProperty, null);
                }
            }
        } else if (callSiteDescriptor.getName().startsWith("dynjs:compile")) {
            if (callSiteDescriptor.getNameTokenCount() == 3) {
                if ("lookup".equals(callSiteDescriptor.getNameToken(2))) {
                    MethodType type = methodType(CodeBlock.class, int.class);
                    MethodHandle retrieveMH = lookup().findVirtual(DynThreadContext.class, "retrieve", type);
                    return new GuardedInvocation(retrieveMH, null);
                } else if ("function".equals(callSiteDescriptor.getNameToken(2))) {
                    MethodType targetType = methodType(Function.class, CodeBlock.class, String[].class);
                    MethodHandle compile = lookup().findVirtual(DynJS.class, "compile", targetType);
                    return new GuardedInvocation(compile, null);
                } else if ("if".equals(callSiteDescriptor.getNameToken(2))) {
                    MethodHandle targetHandle = linkerServices.asType(RT.IF_STATEMENT, callSiteDescriptor.getMethodType());
                    return new GuardedInvocation(targetHandle, null);
                } else if ("params".equals(callSiteDescriptor.getNameToken(2))) {
                    MethodHandle targetHandle = linkerServices.asType(RT.PARAM_POPULATOR, callSiteDescriptor.getMethodType());
                    return new GuardedInvocation(targetHandle, null);
                }
            }
        } else if (callSiteDescriptor.getName().startsWith("dynjs:runtime")) {
            if ("call".equals(callSiteDescriptor.getNameToken(2))) {
                MethodHandle methodHandle = linkerServices.asType(RT.FUNCTION_CALL, callSiteDescriptor.getMethodType());
                return new GuardedInvocation(methodHandle, null);
            } else if ("eq".equals(callSiteDescriptor.getNameToken(2))) {
                MethodHandle methodHandle = linkerServices.asType(RT.EQ, callSiteDescriptor.getMethodType());
                return new GuardedInvocation(methodHandle, null);
            }
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
