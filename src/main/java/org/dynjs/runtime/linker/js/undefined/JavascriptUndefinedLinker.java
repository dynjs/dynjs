package org.dynjs.runtime.linker.js.undefined;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class JavascriptUndefinedLinker extends ContextualLinker {

    public JavascriptUndefinedLinker(LinkLogger logger) {
        super(logger);
    }

    /*
    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSObjectPropertyGetLink( invocation.builder() );
    }
    */

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSUndefinedPropertySetLink( invocation.builder() );
    }

}
