package org.dynjs.runtime.linker.java.clazz;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.java.clazz.JavaClassPropertyLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

public class JSJavaClassPropertyLinker extends JavaClassPropertyLinker {


    public JSJavaClassPropertyLinker(LinkLogger logger, ResolverManager resolverManager) throws Exception {
        super( resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSJavaClassPropertyGetLink( invocation.builder(), resolverManager );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSJavaClassPropertySetLink( invocation.builder(), resolverManager );
    }
}
