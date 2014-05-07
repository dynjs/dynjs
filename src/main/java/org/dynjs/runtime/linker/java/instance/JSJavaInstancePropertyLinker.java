package org.dynjs.runtime.linker.java.instance;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.java.instance.JavaInstancePropertyLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

public class JSJavaInstancePropertyLinker extends JavaInstancePropertyLinker {


    public JSJavaInstancePropertyLinker(LinkLogger logger, ResolverManager resolverManager) throws NoSuchMethodException, IllegalAccessException {
        super(resolverManager);
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSJavaInstancePropertyGetLink(invocation.builder(), this.resolverManager);
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSJavaInstancePropertySetLink(invocation.builder(), this.resolverManager);
    }
}
