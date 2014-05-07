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
import static org.dynjs.runtime.linker.LinkerUtils.referenceStrictnessFilter;

/**
 * @author Bob McWhirter
 */
public class ShadowObjectPropertySetLink extends AbstractShadowObjectLink {

    public ShadowObjectPropertySetLink(LinkBuilder builder, ShadowObjectManager shadowManager) throws Exception {
        super(builder, shadowManager);
    }

    public MethodHandle target() throws Exception {

        return builder
                .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                .permute(0, 1, 2, 3, 0)
                .filter( 0, ReferenceBaseFilter.INSTANCE )
                .filter( 0, new ShadowFilter( this.shadowManager ) )
                .filter(4, referenceStrictnessFilter())
                .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                .invoke( lookup().findVirtual( JSObject.class, "put", methodType( void.class, ExecutionContext.class, String.class, Object.class, boolean.class )  ))
                .target();
    }

}
