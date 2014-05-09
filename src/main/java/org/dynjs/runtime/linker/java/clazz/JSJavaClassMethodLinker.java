package org.dynjs.runtime.linker.java.clazz;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.java.clazz.JavaClassMethodLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

public class JSJavaClassMethodLinker extends JavaClassMethodLinker {


    public JSJavaClassMethodLinker(LinkLogger logger, ResolverManager resolverManager) throws Exception {
        super( resolverManager);
    }

    @Override
    public Link linkGetMethod(Invocation invocation, String methodName) throws Exception {
        return new JSJavaClassMethodGetLink( invocation.builder(), resolverManager );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new JSJavaClassMethodCallLink( invocation.builder() );
    }

    @Override
    public Link linkConstruct(Invocation invocation) throws Exception {
        return new JSJavaConstructLink( invocation.builder(), resolverManager );
    }
}
