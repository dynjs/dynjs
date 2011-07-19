package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

public class DynObject implements DynAtom {

    private Map<String, Attribute<? extends DynAtom>> attributes = new HashMap<>();

    public Attribute<? extends DynObject> get(String attribute) {
        if (attributes.containsKey(attribute)) {
            return attributes.get(attribute);
        }
        return new Attribute<Undefined>();
    }

    public AttributeBuilder set(String person) {
        return new AttributeBuilder<DynObject>(this, person);
    }

    public void setAttribute(String attributeName, Attribute attribute) {
        attributes.put(attributeName, attribute);
    }

    @Override
    public boolean isUndefined() {
        return false;
    }
}
