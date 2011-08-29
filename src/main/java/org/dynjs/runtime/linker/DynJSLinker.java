package org.dynjs.runtime.linker;

import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.GuardingDynamicLinker;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;

public class DynJSLinker implements GuardingDynamicLinker {
    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        System.out.println(linkRequest);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
