package org.dynjs.runtime.linker.js.shadow;

import org.dynjs.runtime.*;
import org.dynjs.runtime.linker.js.ReferenceBaseFilter;
import org.dynjs.runtime.linker.js.ReferenceStrictnessFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class ShadowObjectPropertySetLink extends AbstractShadowObjectLink implements Guard {

    public ShadowObjectPropertySetLink(LinkBuilder builder, ShadowObjectManager shadowManager) throws Exception {
        super(builder, shadowManager);
        this.builder = builder.guardWith( this );
    }

    public boolean guard(Object receiver, Object context, String propertyName, Object value) {
        if (!(receiver instanceof Reference)) {
            return false;
        }

        Object primary = ((Reference) receiver).getBase();

        if (primary instanceof JSObject || primary instanceof EnvironmentRecord || primary == Types.UNDEFINED) {
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
                .findVirtual(ShadowObjectPropertySetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class, Object.class ))
                .bindTo(this);
    }

    public MethodHandle target() throws Exception {

        return builder
                //.convert(void.class, Reference.class, Object.class, String.class, Object.class)
                .permute(0, 1, 2, 3, 0)
                //.convert(void.class, Reference.class, Object.class, String.class, Object.class, Reference.class)
                .filter(0, ReferenceBaseFilter.INSTANCE)
                .filter(0, new ShadowFilter(this.shadowManager, true))
                .filter(4, ReferenceStrictnessFilter.INSTANCE)
                .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                .invoke(lookup().findVirtual(JSObject.class, "put", methodType(void.class, ExecutionContext.class, String.class, Object.class, boolean.class)))
                .target();
    }

}
