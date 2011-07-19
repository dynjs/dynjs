package org.dynjs.runtime;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class Attributes extends ForwardingMap<String, Attribute<? extends DynObject>> {

    private Map<String, Attribute<? extends DynObject>> attributes = Maps.newHashMap();

    @Override
    protected Map<String, Attribute<? extends DynObject>> delegate() {
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
