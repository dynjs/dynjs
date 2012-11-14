package org.dynjs.runtime.linker;

import java.util.List;

import org.dynalang.dynalink.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.CallSiteDescriptorFactory;

public class BaseDynJSLinker extends AbstractDynJSLinker {

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        GuardedInvocation result = null;
        if (desc.getNameToken(0).equals("dyn")) {
            List<String> ops = CallSiteDescriptorFactory.tokenizeOperators(desc);

            for (String op : ops) {
                if (op.equals( "call" ) ) {
                    result = linkCall(linkRequest, desc);
                }
                if (op.equals( "new" ) ) {
                    result = linkNew(linkRequest, desc);
                }
                if (op.equals( "getProp" ) ) {
                    result = linkGetProp(linkRequest, desc);
                }
                if (op.equals( "getMethod" ) ) {
                    result = linkGetMethod(linkRequest, desc);
                }
                if (result != null) {
                    break;
                }
            }
        }

        return result;
    }
    
    protected GuardedInvocation linkCall(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }


    protected GuardedInvocation linkNew(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

    protected GuardedInvocation linkGetProp(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }
    
    protected GuardedInvocation linkGetMethod(LinkRequest linkRequest, CallSiteDescriptor desc) {
        return null;
    }

}
