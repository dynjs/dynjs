package org.dynjs.parser;

import java.util.HashSet;
import java.util.Set;

public class ObjectLiteralWatcher {
    
    private Set<String> values = new HashSet<>();
    private Set<String> getters = new HashSet<>();
    private Set<String> setters = new HashSet<>();
    
    public ObjectLiteralWatcher() {
        
    }
    
    public boolean addValue(String name) {
        if ( this.values.contains( name ) || this.setters.contains(name) || this.setters.contains(name)) {
            return false;
        }
        this.values.add( name );
        return true;
    }
    
    public boolean addSetter(String name) {
        if ( this.values.contains(name) || this.setters.contains(name)) {
            return false;
        }
        
        this.setters.add( name );
        return true;
    }
    
    public boolean addGetter(String name) {
        if ( this.values.contains(name) || this.getters.contains(name)) {
            return false;
        }
        
        this.getters.add( name );
        return true;
    }

}
