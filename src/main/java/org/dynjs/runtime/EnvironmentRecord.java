package org.dynjs.runtime;

public interface EnvironmentRecord {

    boolean hasBinding(ExecutionContext context, String name);

    void createMutableBinding(ExecutionContext context, String name, boolean deletable);

    Object setMutableBinding(ExecutionContext context, String name, Object value, boolean strict);

    Object getBindingValue(ExecutionContext context, String name, boolean strict);

    boolean deleteBinding(ExecutionContext context, String name);

    Object implicitThisValue();

    boolean isGlobal();

}
