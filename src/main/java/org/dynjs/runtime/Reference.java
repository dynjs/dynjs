package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;

public class Reference {

    private Object base;
    private String referencedName;
    private boolean strict;

    public Reference(String referencedName, Object base, boolean strict) {
        this.referencedName = referencedName;
        this.base = base;
        this.strict = strict;
    }

    public Object getBase() {
        return this.base;
    }

    public String getReferencedName() {
        return this.referencedName;
    }

    public boolean isStrictReference() {
        return this.strict;
    }

    public boolean hasPrimitiveBase() {
        return (this.base instanceof String) || (this.base instanceof Number) || (this.base instanceof Boolean);
    }

    public boolean isPropertyReference() {
        //return (this.base instanceof JSObject) || hasPrimitiveBase();
        return ( ! ( this.base instanceof EnvironmentRecord ) );
    }

    public boolean isUnresolvableReference() {
        return this.base == Types.UNDEFINED;
    }

    public Object getValue(ExecutionContext context) {
        // 8.7.1
        Object value = null;
        if (isUnresolvableReference()) {
            throw new ThrowException(context, context.createReferenceError(referencedName + " is not defined"));
        }
        
        if (isPropertyReference()) {
            if (!hasPrimitiveBase()) {
                value = ((JSObject) this.base).get(context, this.referencedName);
            } else {
                value = primitiveGet(context, Types.toObject(context, this.base), this.referencedName);
            }
        } else {
            value = ((EnvironmentRecord) this.base).getBindingValue(context, this.referencedName, this.strict);
        }
        
        return value;
    }

    protected Object primitiveGet(ExecutionContext context, JSObject o, String name) {
        // 8.7.1 primitive [[Get]]
        Object d = o.getProperty(context, name, false);
        if (d == Types.UNDEFINED) {
            return Types.UNDEFINED;
        }

        PropertyDescriptor desc = (PropertyDescriptor) d;
        if (desc.isDataDescriptor()) {
            Object value = desc.getValue();
            if ( value == null ) {
                value = Types.UNDEFINED;
            }
            return value;
        }

        Object getter = desc.getGetter();

        if (getter == Types.UNDEFINED) {
            return Types.UNDEFINED;
        }

        return context.call((JSFunction) getter, o);
    }

    public void putValue(ExecutionContext context, Object value) {
        // 8.7.2
        if (isUnresolvableReference()) {
            if (isStrictReference()) {
                throw new ThrowException(context, context.createReferenceError(referencedName + " is not defined"));
            } else {
                context.getGlobalContext().getObject().put(context, this.referencedName, value, false);
            }
        } else if (isPropertyReference()) {
            if (!hasPrimitiveBase()) {
                ((JSObject) this.base).put(context, this.referencedName, value, this.strict);
            } else {
                // TODO: handle primitives
            }
        } else {
            ((EnvironmentRecord) this.base).setMutableBinding(context, this.referencedName, value, this.strict);
        }
    }

    public boolean isValidForPrePostIncrementDecrement() {
        if (isStrictReference() && getBase() instanceof EnvironmentRecord) {
            if (this.referencedName.equals("eval") || this.referencedName.equals("arguments")) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "[Reference: name=" + this.referencedName + "; base=" + this.base + " (" + this.base.getClass().getName() + ")]";
    }

}
