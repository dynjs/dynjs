package org.dynjs.runtime.linker.js.environment;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class JavascriptEnvironmentLinker extends ContextualLinker {

    public JavascriptEnvironmentLinker(LinkLogger logger) {
        super(logger);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new EnvironmentPropertyGetLink( invocation.builder() );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new EnvironmentPropertySetLink( invocation.builder() );
    }
}
