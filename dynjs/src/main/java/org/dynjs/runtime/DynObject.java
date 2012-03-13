/**
 *  Copyright 2011 Douglas Campos
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

import org.dynjs.api.Scope;
import org.dynjs.exception.ReferenceError;

import java.util.HashMap;
import java.util.Map;

public class DynObject implements Scope {

    private final Map<String, DynProperty> properties = new HashMap<>();

    public DynObject() {
        setProperty("prototype", DynThreadContext.UNDEFINED);
    }

    public void setProperty(String key, Object atom) {
        DynProperty property = new DynProperty(key).setAttribute("value", atom);
        this.properties.put(key, property);
    }

    public DynProperty getProperty(String key) {
        if (hasOwnProperty(key)) {
            return this.properties.get(key);
        } else {
            throw new IllegalStateException();
        }
    }

    protected boolean hasOwnProperty(String key) {
        return this.properties.containsKey(key);
    }

    @Override
    public Scope getEnclosingScope() {
        Object prototype = getProperty("prototype").getAttribute("value");
        if (prototype instanceof DynObject) {
            return (DynObject) prototype;
        }
        return null;
    }

    @Override
    public Object resolve(String name) {
        if (hasOwnProperty(name)) {
            return this.properties.get(name).getAttribute("value");
        } else if (getEnclosingScope() != null) {
            return getEnclosingScope().resolve(name);
        }
        throw new ReferenceError();
    }

    @Override
    public void define(String property, Object value) {
        setProperty(property, value);
    }

    public static Boolean toBoolean(final Object value) {
        if (value instanceof DynNumber) {
            DynNumber number = (DynNumber) value;
            return !(number.isNaN() || number.getValue() == 0);
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String string = (String) value;
            return !"".equals(string);
        }
        return (value instanceof DynObject);
    }

}
