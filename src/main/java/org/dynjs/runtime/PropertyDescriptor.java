package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;

public class PropertyDescriptor {

    private static final Object defaultValue = Types.UNDEFINED;
    private static final Object defaultSet = Types.UNDEFINED;
    private static final Object defaultGet = Types.UNDEFINED;
    private static final boolean defaultWritable = false;
    private static final boolean defaultConfigurable = false;
    private static final boolean defaultEnumerable = false;

    private Object value;
    private Object set;
    private Object get;
    // FIXME: 4 full words allocs for these flags.  2 bits per flag can use a single word
    // (Java uses full word even when data type is smaller)
    private byte writable;
    private byte configurable;
    private byte enumerable;
    private byte initialized;

    // Values for the byte flags above
    private static final byte UNDEFINED_FLAG = -1;
    private static final byte FALSE_FLAG = 0;
    private static final byte TRUE_FLAG = 1;

    public static PropertyDescriptor newAccessorPropertyDescriptor(Object set, Object get) {
        return newAccessorPropertyDescriptor(set, get, false, false);
    }

    public static PropertyDescriptor newAccessorPropertyDescriptor(Object set, Object get, boolean configurable, boolean enumerable) {
        return new PropertyDescriptor(set, get, configurable, enumerable);
    }

    public static PropertyDescriptor newAccessorPropertyDescriptor() {
        return newAccessorPropertyDescriptor(defaultSet, defaultGet, defaultConfigurable, defaultEnumerable);
    }

    public static PropertyDescriptor newDataPropertyDescriptor(Object value, boolean writable, boolean configurable, boolean enumerable) {
        return new PropertyDescriptor(value, writable, configurable, enumerable);
    }

    public static PropertyDescriptor newDataPropertyDescriptor() {
        return new PropertyDescriptor(defaultValue, defaultWritable, defaultConfigurable, defaultEnumerable);
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializer(Object value) {
        return newPropertyDescriptorForObjectInitializer(null, value);
    }

    public static PropertyDescriptor newPropertyDescriptorForObjectInitializer(String name, Object value) {
        if (name != null && (value instanceof JSFunction)) {
            ((JSFunction) value).setDebugContext("Object." + name);
        }

        return new PropertyDescriptor(value, true, true, true);
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

    private PropertyDescriptor(Object set, Object get, boolean configurable, boolean enumerable) {
        this.set = set;
        this.get = get;
        this.writable = UNDEFINED_FLAG;
        this.configurable = configurable ? TRUE_FLAG : FALSE_FLAG;
        this.enumerable = enumerable ? TRUE_FLAG : FALSE_FLAG;
        this.initialized = UNDEFINED_FLAG;
    }

    private PropertyDescriptor(Object value, boolean writable, boolean configurable, boolean enumerable) {
        this.value = value;
        this.writable = writable ? TRUE_FLAG : FALSE_FLAG;
        this.configurable = configurable ? TRUE_FLAG : FALSE_FLAG;
        this.enumerable = enumerable ? TRUE_FLAG : FALSE_FLAG;
        this.initialized = UNDEFINED_FLAG;
    }

    public PropertyDescriptor() {
        this.writable     = UNDEFINED_FLAG;
        this.configurable = UNDEFINED_FLAG;
        this.enumerable   = UNDEFINED_FLAG;
        this.initialized  = UNDEFINED_FLAG;
    }

    public String toString() {
        return "[PropertyDescriptor value=" + this.value + "; writable=" + this.isWritable() + "; enumerable=" + this.isEnumerable() + "; configurable=" + this.isConfigurable() + "; setter=" + this.set+ "; getter=" + this.get + "]"; 
    }

    public boolean isWritable() {
        return this.writable == TRUE_FLAG ? true : false;
    }

    public Object getWritable() {
        return !hasWritable() ? Types.UNDEFINED : isWritable();
    }

    public boolean hasWritable() {
        return this.writable != UNDEFINED_FLAG;
    }

    public void setWritable(boolean writable) {
        this.writable = writable ? TRUE_FLAG : FALSE_FLAG;
    }

    public boolean isConfigurable() {
        return this.configurable == TRUE_FLAG ? true : false;
    }

    public Object getConfigurable() {
        return !hasConfigurable() ? Types.UNDEFINED : isConfigurable();
    }

    public boolean hasConfigurable() {
        return this.configurable != UNDEFINED_FLAG;
    }

    public void setConfigurable(boolean configurable) {
        this.configurable = configurable ? TRUE_FLAG : FALSE_FLAG;
    }

    public boolean isEnumerable() {
        return this.enumerable == TRUE_FLAG ? true : false;
    }

    public Object getEnumerable() {
        return !hasEnumerable() ? Types.UNDEFINED : isEnumerable();
    }

    public void setEnumerable(boolean enumerable) {
        this.enumerable = enumerable ? TRUE_FLAG : FALSE_FLAG;
    }

    public boolean hasEnumerable() {
        return this.enumerable != UNDEFINED_FLAG;
    }

    public boolean hasInitialized() {
        return this.initialized != UNDEFINED_FLAG;
    }

    public void setInitialized(boolean value) {
        this.initialized = value ? TRUE_FLAG : FALSE_FLAG;
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

    public void setSetter(Object setter) {
        this.set = setter;
    }
    
    public boolean hasSet() {
        return this.set != null;
    }

    public Object getGetter() {
        return this.get;
    }

    public void setGetter(Object getter) {
        this.get = getter;
    }
    
    public boolean hasGet() {
        return this.get != null;
    }

    public boolean isEmpty() {
        return this.value == null && this.set == null && this.get == null &&
                !hasWritable() && !hasConfigurable() && !hasEnumerable();
    }

    public PropertyDescriptor duplicate() {
        // 8.12.1 (steps 2-7)
        PropertyDescriptor d = new PropertyDescriptor();

        if (isDataDescriptor()) {
            // System.err.println("isData");
            d.value = this.value;
            d.writable = this.writable;
        } else {
            // System.err.println("isAccesor");
            d.get = this.get;
            d.set = this.set;
        }

        d.enumerable = this.enumerable;
        d.configurable = this.configurable;

        return d;
    }

    public PropertyDescriptor duplicateWithDefaults() {
        // 8.12.9 (steps 4 a+b)
        PropertyDescriptor d = new PropertyDescriptor();

        if (isGenericDescriptor() || isDataDescriptor()) {
            d.value = this.value != null ? this.value : Types.UNDEFINED;
            d.writable = this.writable == UNDEFINED_FLAG ? FALSE_FLAG : this.writable;
        } else {
            d.get = this.get != null ? this.get : Types.UNDEFINED;
            d.set = this.set != null ? this.set : Types.UNDEFINED;
        }

        d.enumerable = this.enumerable == UNDEFINED_FLAG ? FALSE_FLAG : this.enumerable;
        d.configurable = this.configurable == UNDEFINED_FLAG ? FALSE_FLAG : this.configurable;

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
        if (from.hasWritable()) {
            setWritable(from.isWritable());
        }
        if (from.hasConfigurable()) {
            setConfigurable(from.isConfigurable());
        }
        if (from.hasEnumerable()) {
            setEnumerable(from.isEnumerable());
        }
    }

    public boolean isAccessorDescriptor() {
        // 8.10.1
        return this.get != null || this.set != null;
    }

    public boolean isDataDescriptor() {
        // 8.10.2
        return this.value != null || hasWritable();
    }

    public boolean isGenericDescriptor() {
        // 8.10.3
        return isAccessorDescriptor() == false && isDataDescriptor() == false;
    }

    public static Object fromPropertyDescriptor(ExecutionContext context, Object d) {
        // 8.10.4
        if (d == Types.UNDEFINED) {
            return Types.UNDEFINED;
        }

        final PropertyDescriptor desc = (PropertyDescriptor) d;

        JSObject obj = new DynObject(context.getGlobalContext());

        if (desc.isDataDescriptor()) {
            obj.defineOwnProperty(context, "value",
                    PropertyDescriptor.newDataPropertyDescriptor(desc.getValue(), true, true, true), false);
            obj.defineOwnProperty(context, "writable",
                    PropertyDescriptor.newDataPropertyDescriptor(desc.getWritable(), true, true, true), false);
        } else {
            obj.defineOwnProperty(context, "get",
                    PropertyDescriptor.newDataPropertyDescriptor(desc.getGetter(), true, true, true), false);
            obj.defineOwnProperty(context, "set",
                    PropertyDescriptor.newDataPropertyDescriptor(desc.getSetter(), true, true, true), false);
        }

        obj.defineOwnProperty(context, "enumerable",
                PropertyDescriptor.newDataPropertyDescriptor(desc.getEnumerable(), true, true, true), false);
        obj.defineOwnProperty(context, "configurable",
                PropertyDescriptor.newDataPropertyDescriptor(desc.getConfigurable(), true, true, true), false);

        return obj;
    }

    public static PropertyDescriptor toPropertyDescriptor(ExecutionContext context, Object o) {
        // 8.10.5
        if (!(o instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("attributes must be an object"));
        }

        JSObject obj = (JSObject) o;
        PropertyDescriptor d = new PropertyDescriptor();

        if (obj.hasProperty(context, "enumerable")) {
            d.setEnumerable(Types.toBoolean(obj.get(context, "enumerable")));
        }
        if (obj.hasProperty(context, "configurable")) {
            d.setConfigurable(Types.toBoolean(obj.get(context, "configurable")));
        }
        if (obj.hasProperty(context, "value")) {
            d.setValue(obj.get(context, "value"));
        }
        if (obj.hasProperty(context, "writable")) {
            d.setWritable(Types.toBoolean(obj.get(context, "writable")));
        }
        if (obj.hasProperty(context, "get")) {
            Object getter = obj.get(context, "get");
            if (!Types.isCallable(getter) && getter != Types.UNDEFINED) {
                throw new ThrowException(context, context.createTypeError("get must be callable"));
            }
            d.get = getter;
        }
        if (obj.hasProperty(context, "set")) {
            Object setter = obj.get(context, "set");
            if ((!Types.isCallable(setter)) && setter != Types.UNDEFINED) {
                throw new ThrowException(context, context.createTypeError("set must be callable"));
            }
            d.set = setter;
        }

        if ((d.hasGet() || d.hasSet()) && (d.hasWritable() || d.hasValue())) {
            throw new ThrowException(context, context.createTypeError("may not be both a data property and an accessor property"));
        }

        return d;
    }
}
