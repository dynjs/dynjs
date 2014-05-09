package org.dynjs.runtime.linker.java.instance;

import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.java.instance.JavaBoundMethodLinker;
import org.projectodd.rephract.java.reflect.ResolverManager;

public class JSJavaBoundMethodLinker extends JavaBoundMethodLinker {


    public JSJavaBoundMethodLinker(LinkLogger logger, ResolverManager resolverManager) throws Exception{
        super( resolverManager );
    }

    @Override
    public Link linkCall(Invocation invocation) throws Exception {
        return new JSJavaBoundInstanceMethodCallLink( invocation.builder() );
    }
}

