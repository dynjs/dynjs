package org.dynjs.runtime;

import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.TypeError;

public class DeclarativeEnvironmentRecord implements EnvironmentRecord {

    private Map<String, PropertyDescriptor> mutableBindings = new HashMap<String, PropertyDescriptor>();
    private Map<String, PropertyDescriptor> immutableBindings = new HashMap<String, PropertyDescriptor>();

    @Override
    public boolean hasBinding(String name) {
        return this.mutableBindings.containsKey( name ) || this.immutableBindings.containsKey( name );
    }

    @Override
    public void createMutableBinding(final String name, final boolean configurable) {
        // 10.2.1.1.2
        if ( hasBinding( name ) ) {
            throw new AssertionError( "10.2.1.1.2: Binding already exists for " + name );
        }
        
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set( "Value", DynThreadContext.UNDEFINED );
                set( "Configurable", configurable );
            }
        };
        this.mutableBindings.put( name, desc );
    }

    @Override
    public void setMutableBinding(String name, Object value, boolean strict) {
        // 10.2.1.1.3
        if ( ! hasBinding( name ) ) {
            throw new AssertionError( "10.2.1.1.3: No binding exists for " + name );
        }
        
        if (this.mutableBindings.containsKey( name )) {
            PropertyDescriptor desc = this.mutableBindings.get( name );
            desc.setValue( value );
            return;
        }

        if (strict) {
            throw new TypeError();
        }
    }

    @Override
    public Object getBindingValue(String name, boolean strict) {
        // 10.2.1.1.4
        
        if ( ! hasBinding( name ) ) {
            throw new AssertionError( "10.2.1.1.4: No binding exists for " + name );
        }
        
        PropertyDescriptor desc = this.immutableBindings.get( name );
        if ((desc != null) && (desc.get( "Initialized" ) == null)) {
            if (strict) {
                throw new TypeError();
            }
            return DynThreadContext.UNDEFINED;
        }

        desc = this.mutableBindings.get( name );

        if (desc == null) {
            return DynThreadContext.UNDEFINED;
        }

        return desc.getValue();
    }

    @Override
    public boolean deleteBinding(String name) {
        // 10.2.1.1.5
        PropertyDescriptor desc = this.immutableBindings.get( name );
        if (desc == null) {
            desc = this.mutableBindings.get( name );
        }

        if (desc == null) {
            return true;
        }

        if (!desc.isConfigurable()) {
            return false;
        }

        return (this.mutableBindings.remove( name ) != null);
    }

    @Override
    public Object implicitThisValue() {
        // 10.2.1.1.6
        return DynThreadContext.UNDEFINED;
    }

    public void createImmutableBinding(final String name, final boolean configurable) {
        // 10.2.1.1.7
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set( "Value", DynThreadContext.UNDEFINED );
                set( "Configurable", configurable );
            }
        };
        this.immutableBindings.put( name, desc );

    }

    public void initializeImmutableBinding(String name, Object value) {
        // 10.2.1.1.8
        PropertyDescriptor desc = this.immutableBindings.get( name );
        desc.setValue( value );
        desc.set( "Initialized", Boolean.TRUE );

    }
}
