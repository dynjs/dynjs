package org.dynjs.runtime.linker.js.primitive;

import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

public class JavascriptPrimitiveLinker extends ContextualLinker {

    public JavascriptPrimitiveLinker(LinkLogger logger){
        super(logger);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propName) throws Exception {
        return new PrimitivePropertyGetLink( invocation.builder() );
    }
    
    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new PrimitiveCallLink( invocation.builder() );
    }
}
