package org.dynjs.runtime.linker.js.primitive;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
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
public class PrimitivePropertyGetLink extends SmartLink implements Guard {

    public PrimitivePropertyGetLink(LinkBuilder builder) throws Exception {
        super(builder);
        this.builder = this.builder.guardWith( this );
    }

    public boolean guard(Object receiver, Object context, String propertyName) {
        if ( !(receiver instanceof Reference ) ) {
            return false;
        }
        Object base = ((Reference) receiver).getBase();

        return base instanceof String || base instanceof Number || base instanceof Boolean;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(PrimitivePropertyGetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    protected static Object primitiveReferenceToObject(ExecutionContext context, Reference reference) {
        return Types.toObject( context, reference.getBase() );
    }

    public MethodHandle target() throws Exception {
        return builder
                .convert( Object.class, Reference.class, ExecutionContext.class, String.class )
                .permute( 1, 0, 1, 2 )
                .fold( lookup().findStatic( PrimitivePropertyGetLink.class, "primitiveReferenceToObject", methodType( Object.class, ExecutionContext.class, Reference.class ) ))
                .drop(1, 2)
                .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                .invoke( lookup().findVirtual( JSObject.class, "get", methodType( Object.class, ExecutionContext.class, String.class ) ))
                .target();
    }

}
