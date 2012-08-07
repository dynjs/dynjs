package org.dynjs.runtime;

import org.dynjs.exception.ReferenceError;
import org.dynjs.exception.TypeError;

public class Reference {
    
    private JSObject globalObject;
    private Object base;
    private String referencedName;
    private boolean strict;

    public Reference(JSObject globalObject, String referencedName, Object base, boolean strict) {
        this.globalObject = globalObject;
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
        return false;
    }
    
    public boolean isPropertyReference() {
        return ( this.base instanceof JSObject )  || hasPrimitiveBase();
    }
    
    public boolean isUnresolvableReference() {
        return this.base == DynThreadContext.UNDEFINED;
    }
    
    public Object getValue() {
        // 8.7.1
        Object value = null;
        if ( isUnresolvableReference() ) {
            throw new TypeError();
        }
        
        if ( isPropertyReference() ) {
            if ( ! hasPrimitiveBase() ) {
                value = ((JSObject) this.base).get( this.referencedName );
            } else {
                // TODO: handle primitives (8.7.1 special case)
            }
        } else {
           value = ((EnvironmentRecord) this.base).getBindingValue( this.referencedName, this.strict );
        }
        return value;
    }
    
    public void putValue(Object value) {
        // 8.7.2
        if ( isUnresolvableReference() ) {
            if ( isStrictReference() ) {
                throw new ReferenceError( this.referencedName );
            } else {
                this.globalObject.put(  this.referencedName, value, false );
            }
        } else if ( isPropertyReference() ) {
            if ( ! hasPrimitiveBase() ) {
                ((JSObject) this.base).put( this.referencedName, value, this.strict );
            } else {
                // TODO: handle primitives
            }
        } else {
            ((EnvironmentRecord) this.base).setMutableBinding( this.referencedName, value, this.strict );
        }
    }

}
