package org.dynjs.runtime.linker.java.clazz;

import org.dynjs.runtime.linker.java.ReferenceValueFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.clazz.ClassMethodGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class JSJavaClassMethodGetLink extends ClassMethodGetLink {

    public JSJavaClassMethodGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, String propertyName) {
        return super.guard(ReferenceValueFilter.INSTANCE.filter(receiver), propertyName);
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, ReferenceValueFilter.INSTANCE );
        return super.target();
    }
}
