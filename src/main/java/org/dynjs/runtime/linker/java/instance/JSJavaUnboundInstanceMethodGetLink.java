package org.dynjs.runtime.linker.java.instance;

import org.dynjs.runtime.linker.java.ReferenceValueFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.instance.UnboundInstanceMethodGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class JSJavaUnboundInstanceMethodGetLink extends UnboundInstanceMethodGetLink {

    public JSJavaUnboundInstanceMethodGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, String methodName) {
        return super.guard(ReferenceValueFilter.INSTANCE.filter(receiver), methodName );
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, ReferenceValueFilter.INSTANCE );
        return super.target();
    }
}
