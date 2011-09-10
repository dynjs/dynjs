package org.dynjs.runtime.linker;

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
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynString;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.insertArguments;
import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

public class DynJSLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        CallSiteDescriptor callSiteDescriptor = linkRequest.getCallSiteDescriptor();
        if ("print".equals(callSiteDescriptor.getName())) {
            MethodType methodType = callSiteDescriptor.getMethodType();
            MethodHandle print = lookup().findStatic(RT.class, "print", methodType);

            return new GuardedInvocation(print, null);
        } else if (callSiteDescriptor.getName().startsWith("dyn:getProp")) {
            MethodHandle getProperty = lookup().findVirtual(Scope.class, "resolve", methodType(DynAtom.class, String.class));
            return new GuardedInvocation(getProperty, null);
        }
        return null;
    }

    @Override
    public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType) {
        if(DynString.class.isAssignableFrom(sourceType) && String.class == targetType){
            return Converters.Guarded_DynString2String;
        }
        return null;
    }
}
