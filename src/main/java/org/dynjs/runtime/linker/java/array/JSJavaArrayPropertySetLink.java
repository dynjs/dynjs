package org.dynjs.runtime.linker.java.array;

import org.dynjs.runtime.linker.java.ReferenceValueFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.array.ArrayPropertySetLink;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class JSJavaArrayPropertySetLink extends ArrayPropertySetLink {

    public JSJavaArrayPropertySetLink(LinkBuilder builder) throws Exception {
        super( builder );
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
