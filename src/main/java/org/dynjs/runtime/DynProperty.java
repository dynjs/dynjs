package org.dynjs.runtime;

import org.dynjs.runtime.primitives.DynPrimitiveUndefined;

import java.util.HashMap;
import java.util.Map;

public class DynProperty {

    private final String name;
    private final Map<String, Object> attributes = new HashMap<>();

    public DynProperty(String name) {
        this.name = name;
    }

    public DynProperty setAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public Object getAttribute(String key) {
        if (this.attributes.containsKey(key)) {
            return this.attributes.get(key);
        } else {
            return DynPrimitiveUndefined.UNDEFINED;
        }
    }
}
