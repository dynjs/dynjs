package org.dynjs.parser;

import java.util.HashSet;
import java.util.Set;

public class ObjectLiteralWatcher {
    
    private boolean strict;
    private Set<String> values;
    private Set<String> getters;
    private Set<String> setters;
    
    public ObjectLiteralWatcher(boolean strict) {
        this.strict = strict;
        if ( strict ) {
            this.values = new HashSet<>();
            this.getters = new HashSet<>();
            this.setters = new HashSet<>();
        }
    }
    
    public boolean addValue(String name) {
        if ( ! strict ) {
            return true;
        }
        
        if ( this.values.contains( name ) || this.setters.contains(name) || this.setters.contains(name)) {
            return false;
        }
        this.values.add( name );
        return true;
    }
    
    public boolean addSetter(String name) {
        if ( ! strict ) {
            return true;
        }
        
        if ( this.values.contains(name) || this.setters.contains(name)) {
            return false;
        }
        
        this.setters.add( name );
        return true;
    }
    
    public boolean addGetter(String name) {
        if ( ! strict ) {
            return true;
        }
        
        if ( this.values.contains(name) || this.getters.contains(name)) {
            return false;
        }
        
        this.getters.add( name );
        return true;
    }

}
