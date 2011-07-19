package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

public class DynObject implements DynAtom {

    private Map<String, Attribute<? extends DynAtom>> attributes = new HashMap<>();

    public Attribute<? extends DynAtom> get(String attribute) {
        return attributes.get(attribute);
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
