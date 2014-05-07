package org.dynjs.runtime.linker.js.shadow;

import org.dynjs.runtime.JSObject;

/**
 * @author Bob McWhirter
 */
public interface ShadowObjectManager {

    JSObject getShadowObject(Object primary);

    JSObject getShadowObject(Object primary, boolean create);

    void putShadowObject(Object primary, JSObject shadow);
}
