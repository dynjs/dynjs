package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.ThrowException;

public class DeclarativeEnvironmentRecord implements EnvironmentRecord {

    private Map<String, PropertyDescriptor> mutableBindings = new HashMap<String, PropertyDescriptor>();
    private Map<String, PropertyDescriptor> immutableBindings = new HashMap<String, PropertyDescriptor>();

    @Override
    public boolean hasBinding(ExecutionContext context, String name) {
        return this.mutableBindings.containsKey(name) || this.immutableBindings.containsKey(name);
    }

    @Override
    public void createMutableBinding(ExecutionContext context, final String name, final boolean configurable) {
        // 10.2.1.1.2
        if (hasBinding(context, name)) {
            throw new AssertionError("10.2.1.1.2: Binding already exists for " + name);
        }

        PropertyDescriptor desc = new PropertyDescriptor();
        desc.setValue(Types.UNDEFINED);
        desc.setConfigurable(configurable);
        this.mutableBindings.put(name, desc);
    }

    public void createMutableBinding(final String name, final boolean configurable) {
        DeclarativeEnvironmentRecord.this.createMutableBinding(null, name,configurable);
    }

    @Override
    public void setMutableBinding(ExecutionContext context, String name, Object value, boolean strict) {
        // 10.2.1.1.3
        if (!hasBinding(context, name)) {
            throw new AssertionError("10.2.1.1.3: No binding exists for " + name);
        }

        if (this.mutableBindings.containsKey(name)) {
            PropertyDescriptor desc = this.mutableBindings.get(name);
            desc.setValue(value);
            return;
        }

        if (strict) {
            throw new ThrowException(context, context.createTypeError("attempt to change immutable binding is not allowed"));
        }
    }

    public void setMutableBinding(String name, Object value, boolean strict) {
        DeclarativeEnvironmentRecord.this.setMutableBinding(null, name, value, strict);
    }

        // FIXME: describe spec deviance
    public void assignMutableBinding(ExecutionContext context, String name, Object value, boolean configurable, boolean strict) {
        final boolean exists = hasBinding(context, name);
        if (!exists) {
            PropertyDescriptor desc = new PropertyDescriptor();
            desc.setValue(value);
            desc.setConfigurable(configurable);
            this.mutableBindings.put(name, desc);
        } else {
            PropertyDescriptor desc = this.mutableBindings.get(name);
            desc.setValue(value);
            return;
        }
    }

    @Override
    public Object getBindingValue(ExecutionContext context, String name, boolean strict) {
        // 10.2.1.1.4

        if (!hasBinding(context, name)) {
            throw new AssertionError("10.2.1.1.4: No binding exists for " + name);
        }

        PropertyDescriptor desc = this.immutableBindings.get(name);
        if (desc != null && !desc.hasInitialized()) {
            if (strict) {
                throw new ThrowException(context, context.createTypeError(name + " is not initialized"));
            }
            return Types.UNDEFINED;
        }

        if (desc != null) {
            return desc.getValue();
        }

        desc = this.mutableBindings.get(name);

        if (desc == null) {
            return Types.UNDEFINED;
        }

        return desc.getValue();
    }

    @Override
    public boolean deleteBinding(ExecutionContext context, String name) {
        // 10.2.1.1.5
        PropertyDescriptor desc = this.immutableBindings.get(name);
        if (desc == null) {
            desc = this.mutableBindings.get(name);
        }

        if (desc == null) {
            return true;
        }

        if (!desc.isConfigurable()) {
            return false;
        }

        return (this.mutableBindings.remove(name) != null);
    }

    @Override
    public Object implicitThisValue() {
        // 10.2.1.1.6
        return Types.UNDEFINED;
    }

    public void createImmutableBinding(final String name) {
        // 10.2.1.1.7
        PropertyDescriptor desc = new PropertyDescriptor();
        desc.setValue(Types.UNDEFINED);
        this.immutableBindings.put(name, desc);

    }

    public void initializeImmutableBinding(String name, Object value) {
        // 10.2.1.1.8
        PropertyDescriptor desc = this.immutableBindings.get(name);
        desc.setValue(value);
        desc.setInitialized(true);
    }

    public boolean isGlobal() {
        return false;
    }
}
