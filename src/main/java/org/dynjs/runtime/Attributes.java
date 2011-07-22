/**
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
