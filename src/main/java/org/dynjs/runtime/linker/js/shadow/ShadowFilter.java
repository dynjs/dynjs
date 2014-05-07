package org.dynjs.runtime.linker.js.shadow;


import org.dynjs.runtime.JSObject;
import org.projectodd.rephract.filters.Filter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Bob McWhirter
 */
public class ShadowFilter implements Filter {

    private final ShadowObjectManager shadowManager;

    public ShadowFilter(ShadowObjectManager shadowManager) {
        this.shadowManager = shadowManager;
    }

    public JSObject filter(Object primary) {
        return this.shadowManager.getShadowObject( primary, false );
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.lookup().findVirtual( ShadowFilter.class, "filter", MethodType.methodType( JSObject.class, Object.class ) );
    }
}
