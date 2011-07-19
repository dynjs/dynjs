package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

public class DynObject {

    private Map<String, Attribute<? extends DynObject>> attributes = new HashMap<>();

    public Attribute<? extends DynObject> get(String attribute) {
        if (attributes.containsKey(attribute)) {
            return attributes.get(attribute);
        }
        return new Attribute<Undefined>();
    }

}
