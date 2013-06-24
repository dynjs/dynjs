package org.dynjs.runtime;

public interface JSObject {

    JSObject getPrototype();

    String getClassName();

    void setClassName(String className);

    boolean isExtensible();

    Object get(ExecutionContext context, String name);

    Object getOwnProperty(ExecutionContext context, String name);
    Object getOwnProperty(ExecutionContext context, String name, boolean dupe);

    Object getProperty(ExecutionContext context, String name);
    Object getProperty(ExecutionContext context, String name, boolean dupe);

    boolean hasProperty(ExecutionContext context, String name);

    void put(ExecutionContext context, String name, Object value, boolean shouldThrow);

    boolean canPut(ExecutionContext context, String name);

    boolean delete(ExecutionContext context, String name, boolean shouldThrow);

    Object defaultValue(ExecutionContext context, String hint);

    boolean defineOwnProperty(ExecutionContext context, String name, PropertyDescriptor desc, boolean shouldThrow);

    NameEnumerator getOwnPropertyNames();
    NameEnumerator getAllEnumerablePropertyNames();
    NameEnumerator getOwnEnumerablePropertyNames();

    void setPrototype(JSObject prototype);

    void setExtensible(boolean extensible);

    public abstract void defineReadOnlyProperty(final GlobalObject globalObject, String name, final Object value);

    public abstract void defineNonEnumerableProperty(final GlobalObject globalObject, String name, final Object value);
    
    String dumpDebug();

}
