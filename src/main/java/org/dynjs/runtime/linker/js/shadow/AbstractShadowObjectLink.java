package org.dynjs.runtime.linker.js.shadow;

import org.dynjs.runtime.*;
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
public abstract class AbstractShadowObjectLink extends SmartLink implements Guard {

    protected final ShadowObjectManager shadowManager;

    public AbstractShadowObjectLink(LinkBuilder builder, ShadowObjectManager shadowManager) throws Exception {
        super(builder);
        this.shadowManager = shadowManager;
        this.builder = this.builder.guard(0).with(this);
    }

    public boolean guard(Object receiver) {
        if (!(receiver instanceof Reference)) {
            return false;
        }

        Object base = ((Reference) receiver).getBase();

        if (base instanceof JSObject || base instanceof EnvironmentRecord || base == Types.UNDEFINED) {
            return false;
        }

        return true;
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(AbstractShadowObjectLink.class, "guard", methodType(boolean.class, Object.class))
                .bindTo(this);
    }

}
