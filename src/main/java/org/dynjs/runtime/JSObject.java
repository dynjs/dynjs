package org.dynjs.runtime;

public interface JSObject {
    
    JSObject getPrototype();
    String getClassName();
    boolean isExtensible();
    Object get(String name);
    Object getOwnProperty(String name);
    Object getProperty(String name);
    boolean hasProperty(String name);
    void put(String name, Object value, boolean shouldThrow);
    boolean canPut(String name);
    boolean delete(String name, boolean flag);
    Object defaultValue(String hint);
    boolean defineOwnProperty(String name, PropertyDescriptor desc, boolean flag);

}
