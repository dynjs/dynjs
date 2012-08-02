/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

import java.util.HashMap;
import java.util.Map;

public class DynObject implements Scope {

    private final Map<String, DynProperty> properties = new HashMap<>();
    public Object prototype = DynThreadContext.UNDEFINED;
    private DynObject parent;

    public DynObject() {
    }

    public void setProperty(String key, Object atom) {
        this.properties.put(key, new DynProperty().setAttribute("value", atom));
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
        if (this.prototype instanceof DynObject) {
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
        return null;
    }

    @Override
    public void define(String property, Object value) {
        if (value instanceof DynObject) {
            ((DynObject) value).setParent(this);
        }
        setProperty(property, value);
    }

    public static Boolean toBoolean(final Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Double) {
            final Double valueAsDouble = (Double) value;
            final boolean b = !(valueAsDouble.isNaN() || valueAsDouble == 0.0);
            return b;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String string = (String) value;
            return !"".equals(string);
        } else if (value instanceof DynThreadContext.Undefined ||
                value instanceof DynThreadContext.Null) {
            return false;
        }

        return (value instanceof DynObject);
    }

    public String typeof() {
        if (hasOwnProperty("call")) {
            return "function";
        }
        return "object";
    }

    public Boolean delete(String propertyName) {
        if (hasOwnProperty(propertyName)) {
            DynProperty property = getProperty(propertyName);
            if (property.configurable) {
                this.removeProperty(propertyName);
                return true;
            }
        }
        return false;
    }

    private void removeProperty(String propertyName) {
        properties.remove(propertyName);
    }

    public void setParent(DynObject parent) {
        this.parent = parent;
    }

    public DynObject getParent() {
        return parent;
    }
}
