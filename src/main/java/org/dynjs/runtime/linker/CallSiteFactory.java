package org.dynjs.runtime.linker;

import org.dynjs.runtime.ExecutionContext;
import org.projectodd.rephract.RephractLinker;

import java.lang.invoke.CallSite;

/**
 * @author Bob McWhirter
 */
public class CallSiteFactory {

    private final RephractLinker linker;

    public CallSiteFactory(RephractLinker linker) {
        this.linker = linker;
    }

    public CallSite createGet() {
        try {
            return linker.bootstrap("dyn:getProperty|getMethod", Object.class, Object.class, ExecutionContext.class, String.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public CallSite createSet() {
        try {
            return linker.bootstrap("dyn:setProperty", void.class, Object.class, ExecutionContext.class, String.class, Object.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public CallSite createCall() {
        try {
            return linker.bootstrap("dyn:call", Object.class, Object.class, ExecutionContext.class, Object.class, Object[].class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public CallSite createConstruct() {
        try {
            return linker.bootstrap("dyn:construct", Object.class, Object.class, ExecutionContext.class, Object[].class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
