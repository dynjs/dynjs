package org.dynjs.runtime.linker.js.global;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class GlobalLinker extends ContextualLinker {

    public GlobalLinker(LinkLogger logger) {
        super(logger);
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new GlobalPropertySetLink( invocation.builder() );
    }
}
