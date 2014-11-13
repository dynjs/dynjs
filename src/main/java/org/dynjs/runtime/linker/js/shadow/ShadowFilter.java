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
    private final boolean create;

    public ShadowFilter(ShadowObjectManager shadowManager, boolean create) {
        this.shadowManager = shadowManager;
        this.create = create;
    }

    public JSObject filter(Object primary) {
//        System.err.println( "shadow: " + primary );
        JSObject shadow = this.shadowManager.getShadowObject( primary, this.create );
//        System.err.println( "  with: " + shadow );
        return shadow;
    }

    @Override
    public MethodHandle methodHandle(MethodType inputType) throws Exception {
        return MethodHandles.lookup().findVirtual( ShadowFilter.class, "filter", MethodType.methodType( JSObject.class, Object.class ) )
                .bindTo( this );
    }
}
