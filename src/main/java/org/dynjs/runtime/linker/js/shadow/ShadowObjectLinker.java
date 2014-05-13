package org.dynjs.runtime.linker.js.shadow;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.JSObject;
import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

import java.util.Map;
import java.util.WeakHashMap;

public class ShadowObjectLinker extends ContextualLinker implements ShadowObjectManager {

    private Map<Object, JSObject> shadowObjects = new WeakHashMap<>();

    public ShadowObjectLinker(LinkLogger logger) {
        super(logger);
    }

    public JSObject getShadowObject(Object primary) {
        return getShadowObject(primary, true);
    }
    
    public JSObject getShadowObject(Object primary, boolean create) {
        JSObject shadow = this.shadowObjects.get(primary);
        if (shadow == null && create) {
            shadow = new DynObject();
            this.shadowObjects.put(primary, shadow);
        }
        return shadow;
    }
    
    public void putShadowObject(Object primary, JSObject shadow) {
        this.shadowObjects.put( primary, shadow );
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    @Override
    public Link linkGetProperty(Invocation invocation, String propName) throws Exception {
        return new ShadowObjectPropertyGetLink( invocation.builder(), this );
    }

    @Override
    public Link linkSetProperty(Invocation invocation, String propName) throws Exception {
        return new ShadowObjectPropertySetLink( invocation.builder(), this );
    }

}
