package org.dynjs.runtime.linker.java.instance;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.java.DereferencingFilter;
import org.dynjs.runtime.linker.java.NullReplacingFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.instance.InstancePropertyGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class JSJavaInstancePropertyGetLink extends InstancePropertyGetLink {

    public JSJavaInstancePropertyGetLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, String propertyName) {
        return super.guard(DereferencingFilter.INSTANCE.filter(receiver), propertyName);
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, DereferencingFilter.INSTANCE );
        MethodHandle target = super.target();
        return MethodHandles.filterReturnValue(target, NullReplacingFilter.INSTANCE.methodHandle(MethodType.methodType(Object.class)));
    }
}
