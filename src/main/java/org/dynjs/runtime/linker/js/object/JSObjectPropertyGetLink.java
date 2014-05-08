package org.dynjs.runtime.linker.js.object;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.js.ReferenceBaseFilter;
import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class JSObjectPropertyGetLink extends SmartLink implements Guard {

    public JSObjectPropertyGetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith( this );
    }

    public boolean guard(Object receiver, Object context, String propertyName) {
        return (receiver instanceof Reference) && (((Reference) receiver).getBase() instanceof JSObject);
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(JSObjectPropertyGetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                //.permute(0, 1, 2, 3 )
                .filter(0, ReferenceBaseFilter.INSTANCE)
                .convert( Object.class, JSObject.class, ExecutionContext.class, String.class )
                .invoke(lookup().findVirtual(JSObject.class, "get", methodType(Object.class, ExecutionContext.class, String.class)))
                .target();

    }

}
