package org.dynjs.runtime;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class Attributes extends ForwardingMap<String, Attribute<? extends DynAtom>> {

    private Map<String, Attribute<? extends DynAtom>> attributes = Maps.newHashMap();

    @Override
    protected Map<String, Attribute<? extends DynAtom>> delegate() {
        return this.attributes;
    }

    public Attribute get(String attribute) {
        if (attributes.containsKey(attribute)) {
            return attributes.get(attribute);
        } else if (attributes.containsKey("prototype")) {
            final Attribute<? extends DynAtom> prototype = attributes.get("prototype");
            if (!prototype.isUndefined()) {
                if (prototype.value() instanceof DynObject) {
                    final DynObject value = (DynObject) prototype.value();
                    return value.get(attribute);
                }
            }
        }
        return new Attribute<Undefined>(Undefined.UNDEFINED);
    }
}
