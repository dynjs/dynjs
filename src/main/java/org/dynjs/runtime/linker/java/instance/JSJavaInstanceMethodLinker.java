package org.dynjs.runtime.linker.java.instance;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.java.instance.JavaInstanceMethodLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

public class JSJavaInstanceMethodLinker extends JavaInstanceMethodLinker {


    public JSJavaInstanceMethodLinker(LinkLogger logger, ResolverManager resolverManager) throws Exception{
        super( resolverManager );
    }

    @Override
    public Link linkGetMethod(Invocation invocation, String methodName) throws Exception {
        return new JSJavaUnboundInstanceMethodGetLink( invocation.builder(), resolverManager );
    }

    @Override
    public Link linkGetProperty(Invocation invocation, String methodName) throws Exception {
        return new JSJavaUnboundInstanceMethodGetLink( invocation.builder(), resolverManager );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new JSJavaUnboundInstanceMethodCallLink( invocation.builder() );
    }
}

