package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.ThrowException;

public class PropertyDescriptor {

    // because enums are a full-fledged class that take
    // too much memory
    public static final class Names {
        public static final byte VALUE = 0;
        public static final byte SET = 1;
        public static final byte GET = 2;
        public static final byte WRITABLE = 3;
        public static final byte CONFIGURABLE = 4;
        public static final byte ENUMERABLE = 5;
        public static final byte INITIALIZED = 6;
    }

    private static final Object defaultValue = Types.UNDEFINED;
    private static final Object defaultSet = Types.UNDEFINED;
    private static final Object defaultGet = Types.UNDEFINED;
    private static final boolean defaultWritable = false;
    private static final boolean defaultConfigurable = false;
    private static final boolean defaultEnumerable = false;

    private Object value;
    private Object set;
    private Object get;
    private byte writable     = -1;
    private byte configurable = -1;
    private byte enumerable   = -1;
    private byte initialized  = -1;

    // Values for the byte flags above
    private static final byte UNDEFINED_FLAG = -1;
    private static final byte FALSE_FLAG = 0;
    private static final byte TRUE_FLAG = 1;

    public static PropertyDescriptor newAccessorPropertyDescriptor(final boolean withDefaults) {
        return new PropertyDescriptor() {
            {
                if (withDefaults) {
                    set(Names.SET, defaultSet);
                    set(Names.GET, defaultGet);
                    set(Names.CONFIGURABLE, defaultConfigurable);
                    set(Names.ENUMERABLE, defaultEnumerable);
                }
            }
        };
    }

    public static PropertyDescriptor newDataPropertyDescriptor(final boolean withDefaults) {
        return new PropertyDescriptor() {
            {
                if (withDefaults) {
                    set(Names.VALUE, defaultValue);
                    set(Names.WRITABLE, defaultWritable);
                    set(Names.CONFIGURABLE, defaultConfigurable);
                    set(Names.ENUMERABLE, defaultEnumerable); 
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
        d.setWritable(true);
        d.setConfigurable(true);
        d.setEnumerable(true);
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
        d.setConfigurable(true);
        d.setEnumerable(true);
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
        d.setConfigurable(true);
        d.setEnumerable(true);
        return d;
    }

    public PropertyDescriptor() {
    }

    public String toString() {
        return "[PropertyDescriptor value=" + this.value + "; writable=" + this.isWritable() + "; enumerable=" + this.isEnumerable() + "; configurable=" + this.isConfigurable() + "; setter=" + this.set+ "; getter=" + this.get + "]"; 
    }

    private Boolean getFlag(byte name) {
        switch(name) {
        case Names.WRITABLE:
            return byteToBoolean(this.writable);
        case Names.CONFIGURABLE:
            return byteToBoolean(this.configurable);
        case Names.ENUMERABLE:
            return byteToBoolean(this.enumerable);
        case Names.INITIALIZED:
            return byteToBoolean(this.initialized);
        }
        return null;
    }

    private void setFlag(byte name, Object value) {
        switch(name) {
        case Names.WRITABLE:
            this.writable = objectToByte(value);
            break;
        case Names.CONFIGURABLE:
            this.configurable = objectToByte(value);
            break;
        case Names.ENUMERABLE:
            this.enumerable = objectToByte(value);
            break;
        case Names.INITIALIZED:
            this.initialized = objectToByte(value);
            break;
        }
    }

    private static Boolean byteToBoolean(byte value) {
        return value == UNDEFINED_FLAG ? null : value == TRUE_FLAG ? true : false;
    }

    private static byte objectToByte(Object value) {
        return value == Types.UNDEFINED ? UNDEFINED_FLAG : ((Boolean) value) ? TRUE_FLAG : FALSE_FLAG;
    }

    public boolean isWritable() {
        Boolean writable = getFlag(Names.WRITABLE);
        if (writable == null) {
            return false;
        }
        return writable;
    }

    public boolean hasWritable() {
        return getFlag(Names.WRITABLE) != null;
    }

    public void setWritable(boolean writable) {
        setFlag(Names.WRITABLE, writable);
    }

    public boolean isConfigurable() {
        Boolean configurable = getFlag(Names.CONFIGURABLE);
        if (configurable == null) {
            return false;
        }

        return configurable;
    }

    public boolean hasConfigurable() {
        return getFlag(Names.CONFIGURABLE) != null;
    }

    public void setConfigurable(boolean configurable) {
        setFlag(Names.CONFIGURABLE, configurable);
    }

    public boolean isEnumerable() {
        Boolean enumerable = getFlag(Names.ENUMERABLE);
        if (enumerable == null) {
            return false;
        }

        return enumerable;
    }

    public void setEnumerable(boolean enumerable) {
        setFlag(Names.ENUMERABLE, enumerable);
    }

    public boolean hasEnumerable() {
        return getFlag(Names.ENUMERABLE) != null;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public boolean hasValue() {
        return this.value != null;
    }

    public Object getSetter() {
        return this.set;
    }

    public void setSetter(JSFunction setter) {
        this.set = setter;
    }
    
    public boolean hasSet() {
        return this.set != null;
    }

    public Object getGetter() {
        return this.get;
    }

    public void setGetter(JSFunction getter) {
        this.get = getter;
    }
    
    public boolean hasGet() {
        return this.get != null;
    }

    public boolean isEmpty() {
        return this.value == null && this.set == null && this.get == null &&
            getFlag(Names.WRITABLE) == null &&
            getFlag(Names.CONFIGURABLE) == null &&
            getFlag(Names.ENUMERABLE) == null;
    }

    public void set(byte name, Object value) {
        switch (name) {
        case Names.VALUE:
            this.value = value;
            break;
        case Names.SET:
            this.set = value;
            break;
        case Names.GET:
            this.get = value;
            break;
        case Names.WRITABLE:
        case Names.CONFIGURABLE:
        case Names.ENUMERABLE:
        case Names.INITIALIZED:
            setFlag(name, value);
            break;
        }
    }

    public Object get(byte name) {
        switch (name) {
        case Names.VALUE:
            return (this.value != null ? this.value : Types.UNDEFINED);
        case Names.SET:
            return (this.set != null ? this.set : Types.UNDEFINED);
        case Names.GET:
            return (this.get != null ? this.get : Types.UNDEFINED);
        case Names.WRITABLE:
        case Names.CONFIGURABLE:
        case Names.ENUMERABLE:
        case Names.INITIALIZED:
            Boolean value = getFlag(name);
            return (value != null ? value : Types.UNDEFINED); 
        }
        return Types.UNDEFINED;
    }

    public boolean isPresent(byte name) {
        switch (name) {
        case Names.VALUE:
            return this.value != null;
        case Names.SET:
            return this.set != null;
        case Names.GET:
            return this.get != null;
        case Names.WRITABLE:
        case Names.CONFIGURABLE:
        case Names.ENUMERABLE:
            return getFlag(name) != null;
        }
        return false;
    }

    public Object getWithDefault(byte name) {
        Object v = get(name);
        if (v == null) {
            switch (name) {
            case Names.VALUE:
                return defaultValue;
            case Names.SET:
                return defaultSet;
            case Names.GET:
                return defaultGet;
            case Names.WRITABLE:
                return defaultWritable;
            case Names.CONFIGURABLE:
                return defaultConfigurable;
            case Names.ENUMERABLE:
                return defaultEnumerable;
            }
        }

        return v;
    }

    public PropertyDescriptor duplicate(byte... attributes) {
        PropertyDescriptor d = new PropertyDescriptor();
        for (int i = 0; i < attributes.length; ++i) {
            switch (attributes[i]) {
            case Names.VALUE:
                d.value = this.value;
                break;
            case Names.SET:
                d.set = this.set;
                break;
            case Names.GET:
                d.get = this.get;
                break;
            case Names.WRITABLE:
            case Names.CONFIGURABLE:
            case Names.ENUMERABLE:
                d.setFlag(attributes[i], getFlag(attributes[i]));
                break;
            }
        }
        return d;
    }

    public PropertyDescriptor duplicateWithDefaults(byte... attributes) {
        PropertyDescriptor d = new PropertyDescriptor();
        for (int i = 0; i < attributes.length; ++i) {
            switch (attributes[i]) {
            case Names.VALUE:
                d.value = (this.value != null ? this.value : Types.UNDEFINED);
                break;
            case Names.SET:
                d.set = (this.set != null ? this.set : Types.UNDEFINED);
                break;
            case Names.GET:
                d.get = (this.get != null ? this.get : Types.UNDEFINED);
                break;
            case Names.WRITABLE:
            case Names.CONFIGURABLE:
            case Names.ENUMERABLE:
                Boolean thisValue = getFlag(attributes[i]);
                d.setFlag(attributes[i], thisValue != null ? thisValue : false);
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
        if (from.getFlag(Names.WRITABLE) != null) {
            setFlag(Names.WRITABLE, from.getFlag(Names.WRITABLE));
        }
        if (from.getFlag(Names.CONFIGURABLE) != null) {
            setFlag(Names.CONFIGURABLE, from.getFlag(Names.CONFIGURABLE));
        }
        if (from.getFlag(Names.ENUMERABLE) != null) {
            setFlag(Names.ENUMERABLE, from.getFlag(Names.ENUMERABLE));
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
        if (this.value == null && getFlag(Names.WRITABLE) == null) {
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
                    set(Names.VALUE, desc.get(Names.VALUE));
                    set(Names.WRITABLE, true);
                    set(Names.CONFIGURABLE, true);
                    set(Names.ENUMERABLE, true);
                }
            }, false);
            obj.defineOwnProperty(context, "writable", new PropertyDescriptor() {
                {
                    set(Names.VALUE, desc.get(Names.WRITABLE));
                    set(Names.WRITABLE, true);
                    set(Names.CONFIGURABLE, true);
                    set(Names.ENUMERABLE, true);
                }
            }, false);
        } else {
            obj.defineOwnProperty(context, "get", new PropertyDescriptor() {
                {
                    set(Names.VALUE, desc.get(Names.GET));
                    set(Names.WRITABLE, true);
                    set(Names.CONFIGURABLE, true);
                    set(Names.ENUMERABLE, true);
                }
            }, false);
            obj.defineOwnProperty(context, "set", new PropertyDescriptor() {
                {
                    set(Names.VALUE, desc.get(Names.SET));
                    set(Names.WRITABLE, true);
                    set(Names.CONFIGURABLE, true);
                    set(Names.ENUMERABLE, true);
                }
            }, false);
        }

        obj.defineOwnProperty(context, "enumerable", new PropertyDescriptor() {
            {
                set(Names.VALUE, desc.get(Names.ENUMERABLE));
                set(Names.WRITABLE, true);
                set(Names.CONFIGURABLE, true);
                set(Names.ENUMERABLE, true);
            }
        }, false);
        obj.defineOwnProperty(context, "configurable", new PropertyDescriptor() {
            {
                set(Names.VALUE, desc.get(Names.CONFIGURABLE));
                set(Names.WRITABLE, true);
                set(Names.CONFIGURABLE, true);
                set(Names.ENUMERABLE, true);
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
            d.set(Names.ENUMERABLE, Types.toBoolean(obj.get(context, "enumerable")));
        }
        if (obj.hasProperty(context, "configurable")) {
            d.set(Names.CONFIGURABLE, Types.toBoolean(obj.get(context, "configurable")));
        }
        if (obj.hasProperty(context, "value")) {
            d.set(Names.VALUE, obj.get(context, "value"));
        }
        if (obj.hasProperty(context, "writable")) {
            d.set(Names.WRITABLE, Types.toBoolean(obj.get(context, "writable")));
        }
        if (obj.hasProperty(context, "get")) {
            Object getter = obj.get(context, "get");
            if ((!Types.isCallable(getter)) && (getter != Types.UNDEFINED)) {
                throw new ThrowException(context, context.createTypeError("get must be callable"));
            }
            d.set(Names.GET, getter);
        }
        if (obj.hasProperty(context, "set")) {
            Object setter = obj.get(context, "set");
            if ((!Types.isCallable(setter)) && (setter != Types.UNDEFINED)) {
                throw new ThrowException(context, context.createTypeError("set must be callable"));
            }
            d.set(Names.SET, setter);
        }

        if ((d.get(Names.GET) != Types.UNDEFINED) || (d.get(Names.SET) != Types.UNDEFINED)) {
            if ((d.get(Names.WRITABLE) != Types.UNDEFINED) || (d.get(Names.VALUE) != Types.UNDEFINED)) {
                throw new ThrowException(context, context.createTypeError("may not be both a data property and an accessor property"));
            }
        }

        return d;
    }
}
