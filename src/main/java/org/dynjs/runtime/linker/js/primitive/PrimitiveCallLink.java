package org.dynjs.runtime.linker.js.primitive;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class PrimitiveCallLink extends SmartLink implements Guard {

    public PrimitiveCallLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith( this );
    }

    public boolean guard(Object receiver, Object context, Object self, Object[] args) {
        if ( !(receiver instanceof DereferencedReference) ) {
            return false;
        }
        Object base = ((DereferencedReference) receiver).getValue();

        return base instanceof String || base instanceof Number || base instanceof Boolean;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(PrimitiveCallLink.class, "guard", methodType(boolean.class, Object.class, Object.class, Object.class, Object[].class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    protected static Object primitiveReferenceToObject(ExecutionContext context, Reference reference) {
        return Types.toObject( context, reference.getBase() );
    }


    public static ThrowException typeError(ExecutionContext context, Object nonCallable) {
        return new ThrowException(context, context.createTypeError("not callable: " + nonCallable));
    }


    public MethodHandle target() throws Exception {
        return builder
                .permute(1, 2)
                .convert(Object.class, ExecutionContext.class, Object.class)
                .fold(lookup().findStatic(PrimitiveCallLink.class, "typeError", MethodType.methodType(ThrowException.class, ExecutionContext.class, Object.class)))
                .drop(1, 2)
                .invoke(MethodHandles.throwException(Object.class, ThrowException.class))
                .target();
    }

}
