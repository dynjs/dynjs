package org.dynjs.runtime.linker.js.shadow;

import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;

import java.lang.invoke.MethodHandle;

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
