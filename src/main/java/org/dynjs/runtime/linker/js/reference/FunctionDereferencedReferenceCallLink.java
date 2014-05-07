package org.dynjs.runtime.linker.js.reference;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.linker.java.DereferencedReferenceFilter;
import org.dynjs.runtime.linker.java.ReferenceValueFilter;
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
public class FunctionDereferencedReferenceCallLink extends SmartLink implements Guard {

    public FunctionDereferencedReferenceCallLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith(this);
    }

    public boolean guard(Object receiver, Object context, Object self, Object[] args) {
        return (receiver instanceof DereferencedReference) && (((DereferencedReference) receiver).getValue() instanceof JSFunction);
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(FunctionDereferencedReferenceCallLink.class, "guard", methodType(boolean.class, Object.class, Object.class, Object.class, Object[].class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {

        return builder
                .permute(1, 0, 0, 2, 3)
                .filter(1, DereferencedReferenceFilter.INSTANCE)
                .filter(2, ReferenceValueFilter.INSTANCE)
                .convert(Object.class, ExecutionContext.class, Object.class, JSFunction.class, Object.class, Object[].class)
                .invoke(lookup().findVirtual(ExecutionContext.class, "call", methodType(Object.class, Object.class, JSFunction.class, Object.class, Object[].class)))
                .target();
    }

}
