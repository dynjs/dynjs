package org.dynjs.runtime.linker.java.jsimpl;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class JSJavaImplementationLinker extends ContextualLinker {

    private JSJavaImplementationManager implManager;

    public JSJavaImplementationLinker(JSJavaImplementationManager implManager, LinkLogger logger) {
        super(logger);
        this.implManager = implManager;
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return new JSJavaImplementationConstructLink( invocation.builder(), this.implManager );
    }

}
