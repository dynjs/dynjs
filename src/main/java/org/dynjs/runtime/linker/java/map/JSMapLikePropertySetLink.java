package org.dynjs.runtime.linker.java.map;

import org.dynjs.runtime.linker.java.ReferenceValueFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.map.MapLikePropertySetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class JSMapLikePropertySetLink extends MapLikePropertySetLink {

    public JSMapLikePropertySetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, String propertyName, Object value) {
        return super.guard(ReferenceValueFilter.INSTANCE.filter(receiver), propertyName, value);
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, ReferenceValueFilter.INSTANCE );
        return super.target();
    }
}
