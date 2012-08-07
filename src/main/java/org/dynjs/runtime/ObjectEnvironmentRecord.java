package org.dynjs.runtime;

import org.dynjs.exception.ReferenceError;

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

    @Override
    public boolean hasBinding(String name) {
        // 10.2.1.2.1
        return this.object.hasProperty( name );
    }

    @Override
    public void createMutableBinding(final String name, final boolean configValue) {
        // 10.2.1.2.2
        PropertyDescriptor desc = new PropertyDescriptor() {{
            set( "Value", DynThreadContext.UNDEFINED );
            set( "Writable", true );
            set( "Enumerable", true );
            set( "Configurable", configValue );
        }};
        this.object.defineOwnProperty( name, desc, true);
    }

    @Override
    public void setMutableBinding(String name, Object value, boolean strict) {
        // 10.2.1.2.3
        this.object.put( name, value, strict );
    }

    @Override
    public Object getBindingValue(String name, boolean strict) {
        // 10.2.1.2.4
        Object d = this.object.getProperty( name );
        if ( d == DynThreadContext.UNDEFINED ) {
            if ( strict ) {
                throw new ReferenceError( name );
            }
            return DynThreadContext.UNDEFINED;
        }
        PropertyDescriptor desc = (PropertyDescriptor) d;
        return desc.getValue();
    }

    @Override
    public boolean deleteBinding(String name) {
        // 10.2.1.2.4
        return this.object.delete( name, false );
    }

    @Override
    public Object implicitThisValue() {
        // 10.2.1.2.6
        if ( provideThis ) {
            return this.object;
        }
        
        return DynThreadContext.UNDEFINED;
    }

}
