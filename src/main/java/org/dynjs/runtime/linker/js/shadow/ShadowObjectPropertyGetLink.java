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
import static org.dynjs.runtime.linker.LinkerUtils.referenceBaseFilter;
import static org.dynjs.runtime.linker.LinkerUtils.referenceStrictnessFilter;

/**
 * @author Bob McWhirter
 */
public class ShadowObjectPropertyGetLink extends AbstractShadowObjectLink {

    public ShadowObjectPropertyGetLink(LinkBuilder builder, ShadowObjectManager shadowManager) throws Exception {
        super(builder, shadowManager);
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .permute(0, 1, 2, 3)
                .filter(0, ReferenceBaseFilter.INSTANCE)
                .filter(0, new ShadowFilter( this.shadowManager ) )
                .invoke(lookup().findVirtual(JSObject.class, "get", methodType(Object.class, ExecutionContext.class, String.class)))
                .target();

    }

}
