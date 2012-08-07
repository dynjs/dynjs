package org.dynjs.runtime;

public interface EnvironmentRecord {
    
    boolean hasBinding(String name);
    void createMutableBinding(String name, boolean deletable);
    void setMutableBinding(String name, Object value, boolean strict);
    Object getBindingValue(String name, boolean strict);
    boolean deleteBinding(String name);
    Object implicitThisValue();

}
