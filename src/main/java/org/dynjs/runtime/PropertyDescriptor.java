package org.dynjs.runtime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.ThrowException;

public class PropertyDescriptor {

    private static final Map<String, Object> DEFAULTS = new HashMap<String, Object>() {
        {
            put("Value", Types.UNDEFINED);
            put("Set", Types.UNDEFINED);
            put("Get", Types.UNDEFINED);
            put("Writable", false);
            put("Configurable", false);
            put("Enumerable", false);
        }
    };

    private Object value;
    private Object set;
    private Object get;
    private Object writable;
    private Object configurable;
    private Object enumerable;
    private Object initialized;

    public static PropertyDescriptor newAccessorPropertyDescriptor(final boolean withDefaults) {
        return new PropertyDescriptor() {
            {
                if (withDefaults) {
                    set("Value", DEFAULTS.get("Value"));
                    set("Writable", DEFAULTS.get("Writable"));
                    set("Configurable", DEFAULTS.get("Configurable"));
                    set("Enumerable", DEFAULTS.get("Enumerable"));
                }
            }
        };
    }

    public static PropertyDescriptor newDataPropertyDescriptor(final boolean withDefaults) {
        return new PropertyDescriptor() {
            {
                if (withDefaults) {
                    set("Set", DEFAULTS.get("Set"));
                    set("Get", DEFAULTS.get("Get"));
                    set("Configurable", DEFAULTS.get("Configurable"));
                    set("Enumerable", DEFAULTS.get("Enumerable"));
                }
            }
        };
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializer(Object value) {
        return newPropertyDescriptorForObjectInitializer(null, value);
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializer(String name, Object value) {
        if (name != null && (value instanceof JSFunction)) {
            ((JSFunction) value).setDebugContext("Object." + name);
        }
        PropertyDescriptor d = new PropertyDescriptor();
        d.value = value;
        d.writable = Boolean.TRUE;
        d.configurable = Boolean.TRUE;
        d.enumerable = Boolean.TRUE;
        return d;
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializerGet(Object orig, String name, JSFunction value) {
        value.setDebugContext("Object.get " + name);
        PropertyDescriptor d = null;

        if (orig == Types.UNDEFINED) {
            d = new PropertyDescriptor();
        } else {
            d = (PropertyDescriptor) orig;
        }
        d.get = value;
        d.configurable = Boolean.TRUE;
        d.enumerable = Boolean.TRUE;
        return d;
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializerSet(Object orig, String name, JSFunction value) {
        value.setDebugContext("Object.set " + name);
        PropertyDescriptor d = null;

        if (orig == Types.UNDEFINED) {
            d = new PropertyDescriptor();
        } else {
            d = (PropertyDescriptor) orig;
        }
        d.set = value;
        d.configurable = Boolean.TRUE;
        d.enumerable = Boolean.TRUE;
        return d;
    }

    public PropertyDescriptor() {
    }

    public String toString() {
        return "[PropertyDescriptor value=" + this.value + "; enumerable=" + this.enumerable + "]";// : attributes=" + this.attributes.keySet() + "]";
    }

    public boolean isWritable() {
        if (this.writable == null) {
            return false;
        }
        if (this.writable == Types.UNDEFINED) {
            return false;
        }
        return (Boolean) this.writable;
    }

    public boolean hasWritable() {
        return this.writable != null;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public boolean isConfigurable() {
        if (this.configurable == null) {
            return false;
        }

        if (this.configurable == Types.UNDEFINED) {
            return false;
        }

        return (Boolean) this.configurable;
    }

    public boolean hasConfigurable() {
        return this.configurable != null;
    }

    public void setConfigurable(boolean configurable) {
        this.configurable = configurable;
    }

    public boolean isEnumerable() {
        if (this.enumerable == null) {
            return false;
        }

        if (this.enumerable == Types.UNDEFINED) {
            return false;
        }

        return (Boolean) this.enumerable;
    }

    public void setEnumerable(boolean enumerable) {
        this.enumerable = enumerable;
    }

    public boolean hasEnumerable() {
        return this.enumerable != null;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getSetter() {
        return this.set;
    }

    public void setSetter(JSFunction setter) {
        this.set = setter;
    }

    public Object getGetter() {
        return this.get;
    }

    public void setGetter(JSFunction getter) {
        this.get = getter;
    }

    public boolean isEmpty() {
        return this.value == null && this.set == null && this.get == null &&
                this.writable == null && this.configurable == null && this.enumerable == null;
    }

    public void set(String name, Object value) {
        switch (name) {
        case "Value":
            this.value = value;
            break;
        case "Set":
            this.set = value;
            break;
        case "Get":
            this.get = value;
            break;
        case "Writable":
            this.writable = value;
            break;
        case "Configurable":
            this.configurable = value;
            break;
        case "Enumerable":
            this.enumerable = value;
            break;
        case "Initialized":
            this.initialized = value;
            break;
        }
    }

    public Object get(String name) {
        switch (name) {
        case "Value":
            return (this.value != null ? this.value : Types.UNDEFINED);
        case "Set":
            return (this.set != null ? this.set : Types.UNDEFINED);
        case "Get":
            return (this.get != null ? this.get : Types.UNDEFINED);
        case "Writable":
            return (this.writable != null ? this.writable : Types.UNDEFINED);
        case "Configurable":
            return (this.configurable != null ? this.configurable : Types.UNDEFINED);
        case "Enumerable":
            return (this.enumerable != null ? this.enumerable : Types.UNDEFINED);
        case "Initialized":
            return (this.initialized != null ? this.initialized : Types.UNDEFINED);
        }
        return Types.UNDEFINED;
    }

    public boolean isPresent(String name) {
        switch (name) {
        case "Value":
            return this.value != null;
        case "Set":
            return this.set != null;
        case "Get":
            return this.get != null;
        case "Writable":
            return this.writable != null;
        case "Configurable":
            return this.configurable != null;
        case "Enumerable":
            return this.enumerable != null;
        }
        return false;
    }

    public Object getWithDefault(String name) {
        Object v = get(name);
        if (v == null) {
            return DEFAULTS.get(name);
        }

        return v;
    }

    public PropertyDescriptor duplicate(String... attributes) {
        PropertyDescriptor d = new PropertyDescriptor();
        for (int i = 0; i < attributes.length; ++i) {
            switch (attributes[i]) {
            case "Value":
                d.value = this.value;
                break;
            case "Set":
                d.set = this.set;
                break;
            case "Get":
                d.get = this.get;
                break;
            case "Writable":
                d.writable = this.writable;
                break;
            case "Configurable":
                d.configurable = this.configurable;
                break;
            case "Enumerable":
                d.enumerable = this.enumerable;
                break;
            }
        }
        return d;
    }

    public PropertyDescriptor duplicateWithDefaults(String... attributes) {
        PropertyDescriptor d = new PropertyDescriptor();
        for (int i = 0; i < attributes.length; ++i) {
            switch (attributes[i]) {
            case "Value":
                d.value = (this.value != null ? this.value : Types.UNDEFINED);
                break;
            case "Set":
                d.set = (this.set != null ? this.set : Types.UNDEFINED);
                break;
            case "Get":
                d.get = (this.get != null ? this.get : Types.UNDEFINED);
                break;
            case "Writable":
                d.writable = (this.writable != null ? this.writable : Boolean.FALSE);
                break;
            case "Configurable":
                d.configurable = (this.configurable != null ? this.configurable : Boolean.FALSE);
                break;
            case "Enumerable":
                d.enumerable = (this.enumerable != null ? this.enumerable : Boolean.FALSE);
                break;
            }
        }
        return d;
    }

    public void copyAll(PropertyDescriptor from) {
        if (from.value != null) {
            this.value = from.value;
        }
        if (from.set != null) {
            this.set = from.set;
        }
        if (from.get != null) {
            this.get = from.get;
        }
        if (from.writable != null) {
            this.writable = from.writable;
        }
        if (from.configurable != null) {
            this.configurable = from.configurable;
        }
        if (from.enumerable != null) {
            this.enumerable = from.enumerable;
        }
    }

    public boolean isAccessorDescriptor() {
        // 8.10.1
        if (this.get == null && this.set == null) {
            return false;
        }

        return true;
    }

    public boolean isDataDescriptor() {
        // 8.10.2
        if (this.value == null && this.writable == null) {
            return false;
        }

        return true;
    }

    public boolean isGenericDescriptor() {
        // 8.10.3
        if (isAccessorDescriptor() == false && isDataDescriptor() == false) {
            return true;
        }

        return false;
    }

    public static Object fromPropertyDescriptor(ExecutionContext context, Object d) {
        // 8.10.4
        if (d == Types.UNDEFINED) {
            return Types.UNDEFINED;
        }

        final PropertyDescriptor desc = (PropertyDescriptor) d;

        DynObject obj = new DynObject(context.getGlobalObject());

        if (desc.isDataDescriptor()) {
            obj.defineOwnProperty(context, "value", new PropertyDescriptor() {
                {
                    set("Value", desc.get("Value"));
                    set("Writable", true);
                    set("Configurable", true);
                    set("Enumerable", true);
                }
            }, false);
            obj.defineOwnProperty(context, "writable", new PropertyDescriptor() {
                {
                    set("Value", desc.get("Writable"));
                    set("Writable", true);
                    set("Configurable", true);
                    set("Enumerable", true);
                }
            }, false);
        } else {
            obj.defineOwnProperty(context, "get", new PropertyDescriptor() {
                {
                    set("Value", desc.get("Get"));
                    set("Writable", true);
                    set("Configurable", true);
                    set("Enumerable", true);
                }
            }, false);
            obj.defineOwnProperty(context, "set", new PropertyDescriptor() {
                {
                    set("Value", desc.get("Set"));
                    set("Writable", true);
                    set("Configurable", true);
                    set("Enumerable", true);
                }
            }, false);
        }

        obj.defineOwnProperty(context, "enumerable", new PropertyDescriptor() {
            {
                set("Value", desc.get("Enumerable"));
                set("Writable", true);
                set("Configurable", true);
                set("Enumerable", true);
            }
        }, false);
        obj.defineOwnProperty(context, "configurable", new PropertyDescriptor() {
            {
                set("Value", desc.get("Configurable"));
                set("Writable", true);
                set("Configurable", true);
                set("Enumerable", true);
            }
        }, false);

        return obj;
    }

    public static PropertyDescriptor toPropertyDescriptor(ExecutionContext context, Object o) {
        // 8.10.5
        if (!(o instanceof JSObject)) {
            throw new ThrowException(context.createTypeError("attribtues must be an object"));
        }

        JSObject obj = (JSObject) o;
        PropertyDescriptor d = new PropertyDescriptor();

        if (obj.hasProperty(context, "enumerable")) {
            d.set("Enumerable", Types.toBoolean(obj.get(context, "enumerable")));
        }
        if (obj.hasProperty(context, "configurable")) {
            d.set("Configurable", Types.toBoolean(obj.get(context, "configurable")));
        }
        if (obj.hasProperty(context, "value")) {
            d.set("Value", obj.get(context, "value"));
        }
        if (obj.hasProperty(context, "writable")) {
            d.set("Writable", Types.toBoolean(obj.get(context, "writable")));
        }
        if (obj.hasProperty(context, "get")) {
            Object getter = obj.get(context, "get");
            if ((!Types.isCallable(getter)) && (getter != Types.UNDEFINED)) {
                throw new ThrowException(context.createTypeError("get must be callable"));
            }
            d.set("Get", getter);
        }
        if (obj.hasProperty(context, "set")) {
            Object setter = obj.get(context, "set");
            if ((!Types.isCallable(setter)) && (setter != Types.UNDEFINED)) {
                throw new ThrowException(context.createTypeError("set must be callable"));
            }
            d.set("Set", setter);
        }

        if ((d.get("Get") != Types.UNDEFINED) || (d.get("Set") != Types.UNDEFINED)) {
            if ((d.get("Writable") != Types.UNDEFINED) || (d.get("Value") != Types.UNDEFINED)) {
                throw new ThrowException(context.createTypeError("may not be both a data property and an accessor property"));
            }
        }

        return d;
    }
}
