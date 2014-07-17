package org.dynjs.runtime.linker.js.undefined;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.js.GlobalObjectFilter;
import org.dynjs.runtime.linker.js.ReferenceStrictnessFilter;
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
public class JSUndefinedPropertySetLink extends SmartLink implements Guard {

    public JSUndefinedPropertySetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object context, String propertyName, Object value) {
        return (receiver instanceof Reference) && (((Reference) receiver).isUnresolvableReference()) && (!((Reference) receiver).isStrictReference());
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(JSUndefinedPropertySetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class, Object.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                //.convert(void.class, Reference.class, Object.class, String.class, Object.class )
                .permute(1, 1, 2, 3, 0)
                //.convert(void.class, ExecutionContext.class, ExecutionContext.class, String.class, Object.class, Reference.class )
                .filter(0, GlobalObjectFilter.INSTANCE)
                .filter(4, ReferenceStrictnessFilter.INSTANCE)
                .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                .invoke(lookup().findVirtual(JSObject.class, "put", methodType(void.class, ExecutionContext.class, String.class, Object.class, boolean.class)))
                .target();
    }

}
