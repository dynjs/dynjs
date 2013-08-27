package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.PropertyDescriptor.Names;

public class ObjectEnvironmentRecord implements EnvironmentRecord {

    private JSObject object;
    private boolean provideThis;

    public ObjectEnvironmentRecord(JSObject object, boolean provideThis) {
        this.object = object;
        this.provideThis = provideThis;
    }

    public JSObject getBindingObject() {
        return this.object;
    }

    public String toString() {
        return "[ObjectEnvironmentRecord: object=" + this.object + "]";
    }

    @Override
    public boolean hasBinding(ExecutionContext context, String name) {
        // 10.2.1.2.1
        return this.object.hasProperty(context, name);
    }

    @Override
    public void createMutableBinding(ExecutionContext context, final String name, final boolean configValue) {
        // 10.2.1.2.2
        PropertyDescriptor desc = new PropertyDescriptor();
        desc.set(Names.VALUE, Types.UNDEFINED);
        desc.set(Names.WRITABLE, true);
        desc.set(Names.ENUMERABLE, true);
        desc.set(Names.CONFIGURABLE, configValue);
        this.object.defineOwnProperty(context, name, desc, true);
    }

    @Override
    public void setMutableBinding(ExecutionContext context, String name, Object value, boolean strict) {
        // 10.2.1.2.3
        this.object.put(context, name, value, strict);
    }

    @Override
    public Object getBindingValue(ExecutionContext context, String name, boolean strict) {
        // 10.2.1.2.4
        Object d = this.object.getProperty(context, name, false);
        if (d == Types.UNDEFINED) {
            if (strict) {
                throw new ThrowException(context, context.createReferenceError(name + " is not defined"));
            }
            return Types.UNDEFINED;
        }
        PropertyDescriptor desc = (PropertyDescriptor) d;
        return desc.getValue();
    }

    @Override
    public boolean deleteBinding(ExecutionContext context, String name) {
        // 10.2.1.2.4
        return this.object.delete(context, name, false);
    }

    @Override
    public Object implicitThisValue() {
        // 10.2.1.2.6
        if (provideThis) {
            return this.object;
        }

        return Types.UNDEFINED;
    }

    public boolean isGlobal() {
        return (this.object instanceof GlobalObject);
    }

}
