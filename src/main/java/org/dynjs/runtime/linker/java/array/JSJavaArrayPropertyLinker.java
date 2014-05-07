package org.dynjs.runtime.linker.java.array;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.java.array.JavaArrayPropertyLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

public class JSJavaArrayPropertyLinker extends JavaArrayPropertyLinker {


    public JSJavaArrayPropertyLinker(LinkLogger logger, ResolverManager resolverManager) throws Exception {
        super( resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSJavaArrayPropertyGetLink( invocation.builder() );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSJavaArrayPropertySetLink( invocation.builder() );
    }
}
