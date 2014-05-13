package org.dynjs.runtime.linker.java.jsimpl;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
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
public class JSJavaImplementationConstructLink extends SmartLink implements Guard {

    private final JSJavaImplementationManager implManager;

    public JSJavaImplementationConstructLink(LinkBuilder builder, JSJavaImplementationManager implManager) throws Exception {
        super(builder);
        this.implManager = implManager;
        this.builder = this.builder.guardWith( this );
    }

    public boolean guard(Object receiver, Object context, Object[] args) {
        if (!(receiver instanceof Class)) {
            return false;
        }

        if (args.length != 1) {
            return false;
        }

        if (!(args[0] instanceof JSObject)) {
            return false;
        }

        return true;
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(JSJavaImplementationConstructLink.class, "guard", methodType(boolean.class, Object.class, Object.class, Object[].class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    @Override
    public MethodHandle target() throws Exception {
        return builder
                .insert( 0, this.implManager )
                .spread( JSObject.class )
                .convert( Object.class, Class.class, ExecutionContext.class, JSObject.class )
                .invoke( lookup().findVirtual( JSJavaImplementationManager.class, "getImplementationWrapper", methodType( Object.class, Class.class, ExecutionContext.class, JSObject.class )))
                .target();
    }
}
