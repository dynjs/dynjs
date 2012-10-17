package org.dynjs.runtime.linker;

import com.headius.invokebinder.Binder;
import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.GuardingDynamicLinker;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.Guards;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodHandles.lookup;

public class DynJSLinker implements GuardingDynamicLinker {

    public static final MethodHandle GET_VALUE;

    static {
        try {
            GET_VALUE = Binder.from(Object.class, ExecutionContext.class, Object.class)
                    .invokeStatic(lookup(), Types.class, "getValue");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        final CallSiteDescriptor callSiteDescriptor = linkRequest.getCallSiteDescriptor();
        if ("GetValue".equals(callSiteDescriptor.getName())) {
            return new GuardedInvocation(GET_VALUE, Guards.isInstance(Reference.class, 1, callSiteDescriptor.getMethodType()));
        }
        return null;
    }
}
