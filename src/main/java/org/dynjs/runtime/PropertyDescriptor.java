package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.ThrowException;

public class PropertyDescriptor {

    @SuppressWarnings("serial")
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
    private boolean valueDefined;
    private Object set;
    private boolean setDefined;
    private Object get;
    private boolean getDefined;
    private boolean writable;
    private boolean writableDefined;
    private boolean configurable;
    private boolean configurableDefined;
    private boolean enumerable;
    private boolean enumerableDefined;
    private boolean initialized;
    private boolean initializedDefined;

    public static PropertyDescriptor newAccessorPropertyDescriptor(final boolean withDefaults) {
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

    public static PropertyDescriptor newDataPropertyDescriptor(final boolean withDefaults) {
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

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializer(Object value) {
        return newPropertyDescriptorForObjectInitializer(null, value);
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializer(String name, Object value) {
        if (name != null && (value instanceof JSFunction)) {
            ((JSFunction) value).setDebugContext("Object." + name);
        }
        PropertyDescriptor d = new PropertyDescriptor();
        d.value = value;
        d.valueDefined = true;
        d.writable = true;
        d.writableDefined = true;
        d.configurable = true;
        d.configurableDefined = true;
        d.enumerable = true;
        d.enumerableDefined = true;
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
        d.getDefined = true;
        d.configurable = Boolean.TRUE;
        d.configurableDefined = true;
        d.enumerable = Boolean.TRUE;
        d.enumerableDefined = true;
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
        d.setDefined = true;
        d.configurable = Boolean.TRUE;
        d.configurableDefined = true;
        d.enumerable = Boolean.TRUE;
        d.enumerableDefined = true;
        return d;
    }

    public PropertyDescriptor() {
    }

    public String toString() {
        return "[PropertyDescriptor value=" + this.value + "//" + this.valueDefined + "; writable=" + this.writable + "; enumerable=" + this.enumerable
                + "; configurable=" + this.configurable
                + "; setter=" + this.set + "; getter=" + this.get + "]";
    }

    public boolean isWritable() {
        return (this.writableDefined && this.writable);
    }

    public boolean hasWritable() {
        return this.writableDefined;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
        this.writableDefined = true;
    }

    public boolean isConfigurable() {
        return (this.configurableDefined && this.configurable);
    }

    public boolean hasConfigurable() {
        return this.configurableDefined;
    }

    public void setConfigurable(boolean configurable) {
        this.configurable = configurable;
        this.configurableDefined = true;
    }

    public boolean isEnumerable() {
        return (this.enumerableDefined && this.enumerable);
    }

    public void setEnumerable(boolean enumerable) {
        this.enumerable = enumerable;
        this.enumerableDefined = true;
    }

    public boolean hasEnumerable() {
        return this.enumerableDefined;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
        this.valueDefined = true;
    }

    public boolean hasValue() {
        return this.valueDefined;
    }

    public Object getSetter() {
        return this.set;
    }

    public void setSetter(JSFunction setter) {
        this.set = setter;
        this.setDefined = true;
    }

    public boolean hasSet() {
        return this.setDefined;
    }

    public Object getGetter() {
        return this.get;
    }

    public void setGetter(JSFunction getter) {
        this.get = getter;
        this.getDefined = true;
    }

    public boolean hasGet() {
        return this.getDefined;
    }

    public boolean isEmpty() {
        return !(this.valueDefined ||
                this.setDefined ||
                this.getDefined ||
                this.writableDefined ||
                this.configurableDefined || 
                this.enumerableDefined);
    }

    public void set(String name, Object value) {
        switch (name) {
        case "Value":
            this.value = value;
            this.valueDefined = true;
            break;
        case "Set":
            this.set = value;
            this.setDefined = true;
            break;
        case "Get":
            this.get = value;
            this.getDefined = true;
            break;
        case "Writable":
            this.writable = (boolean) value;
            this.writableDefined = true;
            break;
        case "Configurable":
            this.configurable = (boolean) value;
            this.configurableDefined = true;
            break;
        case "Enumerable":
            this.enumerable = (boolean) value;
            this.enumerableDefined = true;
            break;
        case "Initialized":
            this.initialized = (boolean) value;
            this.initializedDefined = true;
            break;
        }
    }

    public Object get(String name) {
        switch (name) {
        case "Value":
            return (this.valueDefined ? this.value : Types.UNDEFINED);
        case "Set":
            return (this.setDefined ? this.set : Types.UNDEFINED);
        case "Get":
            return (this.getDefined ? this.get : Types.UNDEFINED);
        case "Writable":
            return (this.writableDefined ? this.writable : Types.UNDEFINED);
        case "Configurable":
            return (this.configurableDefined ? this.configurable : Types.UNDEFINED);
        case "Enumerable":
            return (this.enumerableDefined ? this.enumerable : Types.UNDEFINED);
        case "Initialized":
            return (this.initializedDefined ? this.initialized : Types.UNDEFINED);
        }
        return Types.UNDEFINED;
    }

    public boolean isPresent(String name) {
        switch (name) {
        case "Value":
            return this.valueDefined;
        case "Set":
            return this.setDefined;
        case "Get":
            return this.getDefined;
        case "Writable":
            return this.writableDefined;
        case "Configurable":
            return this.configurableDefined;
        case "Enumerable":
            return this.enumerableDefined;
        }
        return false;
    }

    public PropertyDescriptor duplicate(String... attributes) {
        PropertyDescriptor d = new PropertyDescriptor();
        for (int i = 0; i < attributes.length; ++i) {
            switch (attributes[i]) {
            case "Value":
                d.value = this.value;
                d.valueDefined = this.valueDefined;
                break;
            case "Set":
                d.set = this.set;
                d.setDefined = this.setDefined;
                break;
            case "Get":
                d.get = this.get;
                d.getDefined = this.getDefined;
                break;
            case "Writable":
                d.writable = this.writable;
                d.writableDefined = this.writableDefined;
                break;
            case "Configurable":
                d.configurable = this.configurable;
                d.configurableDefined = this.configurableDefined;
                break;
            case "Enumerable":
                d.enumerable = this.enumerable;
                d.enumerableDefined = this.enumerableDefined;
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
                if (this.valueDefined) {
                    d.value = this.value;
                    d.valueDefined = true;
                }
                break;
            case "Set":
                if (this.setDefined) {
                    d.set = this.set;
                    d.setDefined = true;
                }
                break;
            case "Get":
                if (this.getDefined) {
                    d.get = this.get;
                    d.getDefined = true;
                }
                break;
            case "Writable":
                if (this.writableDefined) {
                    d.writable = this.writable;
                    d.writableDefined = true;
                }
                break;
            case "Configurable":
                if (this.configurableDefined) {
                    d.configurable = this.configurable;
                    d.configurableDefined = true;
                }
                break;
            case "Enumerable":
                if (this.enumerableDefined) {
                    d.enumerable = this.enumerable;
                    d.enumerableDefined = true;
                }
                break;
            }
        }
        return d;
    }

    public void copyAll(PropertyDescriptor from) {
        if (from.valueDefined) {
            this.value = from.value;
            this.valueDefined = true;
        }
        if (from.setDefined) {
            this.set = from.set;
            this.setDefined = true;
        }
        if (from.getDefined) {
            this.get = from.get;
            this.getDefined = true;
        }
        if (from.writableDefined) {
            this.writable = from.writable;
            this.writableDefined = true;
        }
        if (from.configurableDefined) {
            this.configurable = from.configurable;
            this.configurableDefined = true;
        }
        if (from.enumerableDefined) {
            this.enumerable = from.enumerable;
            this.enumerableDefined = true;
        }
    }

    public boolean isAccessorDescriptor() {
        // 8.10.1
        if ( ! ( this.getDefined || this.setDefined ) ) {
            return false;
        }

        return true;
    }

    public boolean isDataDescriptor() {
        // 8.10.2
        if (!this.valueDefined && !this.writableDefined) {
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

        JSObject obj = new DynObject(context.getGlobalObject());

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
            throw new ThrowException(context, context.createTypeError("attribtues must be an object"));
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
                throw new ThrowException(context, context.createTypeError("get must be callable"));
            }
            d.set("Get", getter);
        }
        if (obj.hasProperty(context, "set")) {
            Object setter = obj.get(context, "set");
            if ((!Types.isCallable(setter)) && (setter != Types.UNDEFINED)) {
                throw new ThrowException(context, context.createTypeError("set must be callable"));
            }
            d.set("Set", setter);
        }

        if ((d.get("Get") != Types.UNDEFINED) || (d.get("Set") != Types.UNDEFINED)) {
            if ((d.get("Writable") != Types.UNDEFINED) || (d.get("Value") != Types.UNDEFINED)) {
                throw new ThrowException(context, context.createTypeError("may not be both a data property and an accessor property"));
            }
        }

        return d;
    }
}
