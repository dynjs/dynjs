package org.dynjs.runtime.linker.js.object;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class JavascriptObjectLinker extends ContextualLinker {

    public JavascriptObjectLinker(LinkLogger logger) {
        super(logger);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSObjectPropertyGetLink( invocation.builder() );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSObjectPropertySetLink( invocation.builder() );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new JSObjectCallLink( invocation.builder() );
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return new JSObjectConstructLink( invocation.builder() );
    }

}
