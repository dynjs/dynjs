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
        } else {
            return new Attribute<Undefined>(new Undefined());
        }
    }
}
