package org.dynjs.runtime.linker.java.clazz;

import org.dynjs.runtime.linker.java.DereferencingFilter;
import org.dynjs.runtime.linker.java.NullReplacingFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.clazz.ClassMethodGetLink;
import org.projectodd.rephract.java.clazz.ClassPropertyGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class JSJavaClassMethodGetLink extends ClassMethodGetLink {

    public JSJavaClassMethodGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, String propertyName) {
        return super.guard(DereferencingFilter.INSTANCE.filter(receiver), propertyName);
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, DereferencingFilter.INSTANCE );
        return super.target();
    }
}
