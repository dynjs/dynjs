package org.dynjs.runtime.linker.js.shadow;

import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.js.ReferenceBaseFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ShadowObjectPropertyGetLink extends AbstractShadowObjectLink implements Guard {

    public ShadowObjectPropertyGetLink(LinkBuilder builder, ShadowObjectManager shadowManager) throws Exception {
        super(builder, shadowManager);
        this.builder = this.builder.guardWith( this );
    }

    public boolean guard(Object receiver, Object context, String propertyName) {
        if (!(receiver instanceof Reference)) {
            return false;
        }

        Object primary = ((Reference) receiver).getBase();

        if (primary instanceof JSObject || primary instanceof EnvironmentRecord || primary == Types.UNDEFINED) {
            return false;
        }

        if ( this.shadowManager.getShadowObject( primary, false ) == null ) {
            return false;
        }

        if ( this.primary != null ) {
            if ( this.primary != primary ) {
                return false;
            }
            if ( ! this.propertyName.equals( propertyName ) ) {
                return false;
            }
        }

        this.primary = primary;
        this.propertyName = propertyName;

        return true;
    }


    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(ShadowObjectPropertyGetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class))
                .bindTo(this);
    }

    public MethodHandle target() throws Exception {
        // primary context name
        return this.builder
                //.permute(0, 1, 2, 3)
                .filter(0, ReferenceBaseFilter.INSTANCE)
                .filter(0, new ShadowFilter( this.shadowManager, false ) )
                .convert( Object.class, JSObject.class, ExecutionContext.class, String.class )
                .invoke(lookup().findVirtual(JSObject.class, "get", methodType(Object.class, ExecutionContext.class, String.class)))
                .target();

    }

}
