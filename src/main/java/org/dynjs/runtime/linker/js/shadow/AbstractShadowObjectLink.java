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
public abstract class AbstractShadowObjectLink extends SmartLink {

    protected final ShadowObjectManager shadowManager;
    protected Object primary;
    protected String propertyName;

    public AbstractShadowObjectLink(LinkBuilder builder, ShadowObjectManager shadowManager) throws Exception {
        super(builder);
        this.shadowManager = shadowManager;
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }


}
