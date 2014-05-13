package org.dynjs.runtime.linker.java.clazz;

import org.dynjs.runtime.linker.java.NullReplacingFilter;
import org.dynjs.runtime.linker.java.ReferenceValueFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.clazz.ClassPropertySetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class JSJavaClassPropertySetLink extends ClassPropertySetLink {

    public JSJavaClassPropertySetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, String propertyName, Object value) {
        return super.guard(ReferenceValueFilter.INSTANCE.filter(receiver), propertyName, value);
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, ReferenceValueFilter.INSTANCE );
        MethodHandle target = super.target();
        return MethodHandles.filterReturnValue(target, NullReplacingFilter.INSTANCE.methodHandle(MethodType.methodType(Object.class)));
    }
}
