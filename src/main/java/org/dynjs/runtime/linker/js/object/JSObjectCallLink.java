package org.dynjs.runtime.linker.js.object;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
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
public class JSObjectCallLink extends SmartLink implements Guard {

    public JSObjectCallLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object context, Object self, Object[] args) {
        return (receiver instanceof JSFunction);
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(JSObjectCallLink.class, "guard", methodType(boolean.class, Object.class, Object.class, Object.class, Object[].class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {

        return this.builder
                .permute(1, 0, 2, 3)
                .convert(Object.class, ExecutionContext.class, JSFunction.class, Object.class, Object[].class)
                .invoke(lookup().findVirtual(ExecutionContext.class, "call", methodType(Object.class, JSFunction.class, Object.class, Object[].class)))
                .target();


    }

}
