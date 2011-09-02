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

import org.dynjs.runtime.primitives.DynPrimitiveUndefined;

import java.util.HashMap;
import java.util.Map;

public class DynObject implements DynAtom {

    private final Map<String, DynProperty> properties = new HashMap<>();

    public DynObject() {
        setProperty("prototype", DynPrimitiveUndefined.UNDEFINED);
    }

    public void setProperty(String key, DynAtom atom) {
        DynProperty property = new DynProperty(key).setAttribute("value", atom);
        this.properties.put(key, property);
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    public DynProperty getProperty(String key) {
        if (this.properties.containsKey(key)) {
            return this.properties.get(key);
        } else {
            throw new IllegalStateException();
        }
    }
}
