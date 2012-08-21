package org.dynjs.runtime;

public interface JSObject {

    JSObject getPrototype();

    String getClassName();

    boolean isExtensible();

    Object get(ExecutionContext context, String name);

    Object getOwnProperty(ExecutionContext context, String name);

    Object getProperty(ExecutionContext context, String name);

    boolean hasProperty(ExecutionContext context, String name);

    void put(ExecutionContext context, String name, Object value, boolean shouldThrow);

    boolean canPut(ExecutionContext context, String name);

    boolean delete(ExecutionContext context, String name, boolean shouldThrow);

    Object defaultValue(String hint);

    boolean defineOwnProperty(ExecutionContext context, String name, PropertyDescriptor desc, boolean shouldThrow);

    NameEnumerator getEnumerablePropertyNames();

    void setPrototype(JSObject prototype);

}
