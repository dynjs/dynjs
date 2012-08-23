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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.ThrowException;

public class DynObject implements JSObject {

    private String className;
    private JSObject prototype = null;

    private final Map<String, PropertyDescriptor> properties = new HashMap<>();
    private boolean extensible = true;

    public DynObject() {
        setClassName("Object");
        setExtensible(true);
    }

    // ------------------------------------------------------------------------
    // JSObject
    // ------------------------------------------------------------------------

    @Override
    public JSObject getPrototype() {
        return this.prototype;
    }

    public void setPrototype(final JSObject prototype) {
        this.prototype = prototype;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean isExtensible() {
        return extensible;
    }

    public void setExtensible(boolean extensible) {
        this.extensible = extensible;
    }

    @Override
    public Object get(ExecutionContext context, String name) {
        // 8.12.3
        if (name.equals("prototype")) {
            if (this.prototype == null) {
                return Types.NULL;
            }
            return this.prototype;
        }
        Object d = getProperty(context, name);
        if (d == Types.UNDEFINED) {
            return Types.UNDEFINED;
        }

        PropertyDescriptor desc = (PropertyDescriptor) d;
        if (desc.isDataDescriptor()) {
            return desc.getValue();
        }

        Object g = desc.getGetter();
        if (g == Types.UNDEFINED) {
            return Types.UNDEFINED;
        }

        JSFunction getter = (JSFunction) g;
        return context.call(getter, this);
    }

    @Override
    public Object getOwnProperty(ExecutionContext context, String name) {
        // 8.12.1
        // Returns PropertyDescriptor or UNDEFINED
        PropertyDescriptor x = this.properties.get(name);

        if (x == null) {
            return Types.UNDEFINED;
        }

        PropertyDescriptor dup = null;
        if (x.isDataDescriptor()) {
            dup = x.duplicate("Value", "Writable", "Enumerable", "Configurable");
        } else {
            dup = x.duplicate("Get", "Set", "Enumerable", "Configurable");
        }

        return dup;
    }

    @Override
    public Object getProperty(ExecutionContext context, String name) {
        // 8.12.2
        // Returns PropertyDescriptor or UNDEFINED
        Object d = getOwnProperty(context, name);
        if (d != Types.UNDEFINED) {
            return d;
        }

        System.err.println("this.proto: " + this.prototype);
        if (this.prototype == null) {
            return Types.UNDEFINED;
        }

        return this.prototype.getProperty(context, name);
    }

    @Override
    public boolean hasProperty(ExecutionContext context, String name) {
        // 8.12.6
        return (getProperty(context, name) != Types.UNDEFINED);
    }

    @Override
    public void put(ExecutionContext context, final String name, final Object value, final boolean shouldThrow) {
        // 8.12.5
        if (!canPut(context, name)) {
            if (shouldThrow) {
                throw new ThrowException(context.createTypeError("cannot put property '" + name + "'"));
            }
            return;
        }

        Object ownDesc = getOwnProperty(context, name);

        if ((ownDesc != Types.UNDEFINED) && ((PropertyDescriptor) ownDesc).isDataDescriptor()) {
            PropertyDescriptor newDesc = new PropertyDescriptor() {
                {
                    set("Value", value);
                }
            };
            defineOwnProperty(context, name, newDesc, shouldThrow);
            return;
        }

        Object desc = getProperty(context, name);

        if ((desc != Types.UNDEFINED) && ((PropertyDescriptor) desc).isAccessorDescriptor()) {
            JSFunction setter = (JSFunction) ((PropertyDescriptor) desc).get("Set");
            context.call(setter, this, value);
        } else {
            PropertyDescriptor newDesc = new PropertyDescriptor() {
                {
                    set("Value", value);
                    set("Writable", true);
                    set("Enumerable", true);
                    set("Configurable", true);
                }
            };
            defineOwnProperty(context, name, newDesc, shouldThrow);
        }
    }

    @Override
    public boolean canPut(ExecutionContext context, String name) {
        // 8.12.4
        Object d = getOwnProperty(context, name);

        // Find the property on ourself, or our prototype
        if (d == Types.UNDEFINED) {
            if (this.prototype != null) {
                d = this.prototype.getProperty(context, name);
            }
        }

        // If either has it, deal with descriptor appropriately
        if (d != Types.UNDEFINED) {
            PropertyDescriptor desc = (PropertyDescriptor) d;
            if (desc.isAccessorDescriptor()) {
                if (desc.get("Set") == Types.UNDEFINED) {
                    return false;
                }
                return true;
            } else {
                Object writable = desc.get("Writable");
                if (writable == Types.UNDEFINED) {
                    return true;
                }
                return (Boolean) writable;
            }
        }

        // Else, just determine if we are extensible directly
        return isExtensible();
    }

    @Override
    public boolean delete(ExecutionContext context, String name, boolean shouldThrow) {
        // 8.12.7
        Object d = getOwnProperty(context, name);
        if (d == Types.UNDEFINED) {
            return true;
        }

        PropertyDescriptor desc = (PropertyDescriptor) d;

        if (desc.get("Configurable") == Boolean.TRUE) {
            this.properties.remove(name);
            return true;
        }

        if (shouldThrow) {
            throw new ThrowException(context.createTypeError("cannot delete unconfigurable property '" + name + "'"));
        }

        return false;
    }

    @Override
    public Object defaultValue(String hint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean defineOwnProperty(ExecutionContext context, String name, PropertyDescriptor desc, boolean shouldThrow) {
        // 8.12.9
        Object c = getOwnProperty(context, name);

        if (c == Types.UNDEFINED) {
            if (!isExtensible()) {
                return reject(context, shouldThrow);
            } else {
                PropertyDescriptor newDesc = null;
                if (desc.isGenericDescriptor() || desc.isDataDescriptor()) {
                    newDesc = desc.duplicateWithDefaults("Value", "Writable", "Enumerable", "Configurable");
                } else {
                    newDesc = desc.duplicateWithDefaults("Get", "Set", "Enumerable", "Configurable");
                }
                this.properties.put(name, newDesc);
                return true;
            }
        }

        if (desc.isEmpty()) {
            return true;
        }

        PropertyDescriptor current = (PropertyDescriptor) c;

        if (current.hasConfigurable() && !current.isConfigurable()) {
            if (desc.hasConfigurable() && desc.isConfigurable()) {
                return reject(context, shouldThrow);
            }
            Object currentEnumerable = current.get("Enumerable");
            Object descEnumerable = desc.get("Enumerable");

            if ((currentEnumerable != Types.UNDEFINED && descEnumerable != Types.UNDEFINED) && (currentEnumerable != descEnumerable)) {
                return reject(context, shouldThrow);
            }
        }

        PropertyDescriptor newDesc = null;

        if (!desc.isGenericDescriptor()) {
            if (current.isDataDescriptor() != desc.isDataDescriptor()) {
                if (!current.isConfigurable()) {
                    return reject(context, shouldThrow);
                }
                if (current.isDataDescriptor()) {
                    newDesc = PropertyDescriptor.newAccessorPropertyDescriptor(true);
                } else {
                    newDesc = PropertyDescriptor.newDataPropertyDescriptor(true);

                }
            } else if (current.isDataDescriptor() && desc.isDataDescriptor()) {
                if (!current.isConfigurable()) {
                    Object currentWritable = current.get("Writable");
                    if ((currentWritable != Types.UNDEFINED) && !current.isWritable()) {
                        if (desc.isWritable()) {
                            return reject(context, shouldThrow);
                        }
                        Object newValue = desc.getValue();
                        if (newValue != null && !Types.sameValue(current.getValue(), newValue)) {
                            return reject(context, shouldThrow);
                        }
                    }
                }
            } else if (current.isAccessorDescriptor() && desc.isAccessorDescriptor()) {
                if (!current.isConfigurable()) {
                    Object newSetter = desc.getSetter();
                    if (newSetter != null && !Types.sameValue(current.getSetter(), newSetter)) {
                        return reject(context, shouldThrow);
                    }
                    Object newGetter = desc.getGetter();
                    if (newGetter != null && !Types.sameValue(current.getGetter(), newGetter)) {
                        return reject(context, shouldThrow);
                    }
                }
            }

            if (newDesc == null) {
                newDesc = new PropertyDescriptor();
            }

            if (current.hasConfigurable()) {
                newDesc.set("Configurable", current.get("Configurable"));
            }
            if (current.hasEnumerable()) {
                newDesc.set("Enumerable", current.get("Enumerable"));
            }
            if (current.hasWritable()) {
                newDesc.set("Writable", current.get("Writable"));
            }

            newDesc.copyAll(desc);
            this.properties.put(name, newDesc);
            return true;
        }

        return false;
    }

    protected boolean reject(ExecutionContext context, boolean shouldThrow) {
        if (shouldThrow) {
            throw new ThrowException( context.createTypeError( "unable to perform operation" ));
        }
        return false;
    }

    @Override
    public NameEnumerator getEnumerablePropertyNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (String name : this.properties.keySet()) {
            PropertyDescriptor desc = this.properties.get(name);
            if (desc.isEnumerable()) {
                names.add(name);
            }
        }
        return new NameEnumerator(names);
    }

}
