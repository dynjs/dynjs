package org.dynjs.runtime.linker.js.reference;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class FunctionDereferencedReferenceLinker extends ContextualLinker {

    public FunctionDereferencedReferenceLinker(LinkLogger logger) {
        super(logger);
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new FunctionDereferencedReferenceCallLink( invocation.builder() );
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return new FunctionDereferencedReferenceConstructLink( invocation.builder() );
    }
}
