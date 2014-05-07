package org.dynjs.runtime.linker.java.instance;

import org.dynjs.runtime.linker.java.DereferencingFilter;
import org.dynjs.runtime.linker.java.NullReplacingFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.instance.UnboundInstanceMethodCallLink;
import org.projectodd.rephract.java.instance.UnboundInstanceMethodGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class JSJavaUnboundInstanceMethodCallLink extends UnboundInstanceMethodCallLink {

    public JSJavaUnboundInstanceMethodCallLink(LinkBuilder builder) throws Exception {
        super( builder );
    }

    @Override
    public boolean guard(Object receiver, Object self, Object[] args) {
        return super.guard(DereferencingFilter.INSTANCE.filter(receiver), self, args );
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, DereferencingFilter.INSTANCE );
        MethodHandle target = super.target();
        return MethodHandles.filterReturnValue( target, NullReplacingFilter.INSTANCE.methodHandle(MethodType.methodType( Object.class ) ) );
    }
}
