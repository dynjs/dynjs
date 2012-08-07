package org.dynjs.runtime;

public class LexicalEnvironment {
    
    public static LexicalEnvironment newDeclarativeEnvironment(LexicalEnvironment outer) {
        return new LexicalEnvironment( new DeclarativeEnvironmentRecord(), outer );
    }
    
    public static LexicalEnvironment newObjectEnvironment(JSObject object, boolean provideThis, LexicalEnvironment outer ) {
        return new LexicalEnvironment( new ObjectEnvironmentRecord( object, provideThis ), outer );
    }
    
    public static LexicalEnvironment newGlobalEnvironment() {
        return new LexicalEnvironment( new ObjectEnvironmentRecord( new GlobalObject(), false ), null );
    }
    
    private LexicalEnvironment outer;
    private EnvironmentRecord record;
    
    private LexicalEnvironment(EnvironmentRecord record, LexicalEnvironment outer) {
        this.record = record;
        this.outer = outer;
    }
    
    public EnvironmentRecord getRecord() {
        return this.record;
    }
    
    public LexicalEnvironment getOuter() {
        return this.outer;
    }
    
    public JSObject getGlobalObject() {
        if ( this.outer == null ) {
            return ((ObjectEnvironmentRecord)this.record).getBindingObject();
        } else {
            return this.outer.getGlobalObject();
        }
    }
    
    public Reference getIdentifierReference(String name, boolean strict) {
        // 10.2.2.1
        boolean exists = record.hasBinding( name );
        if ( exists ) {
            return new Reference( getGlobalObject(), name, this.record, strict );
        }
        
        if ( outer == null ) {
            return new Reference( getGlobalObject(), name, DynThreadContext.UNDEFINED, strict );
        }
        
        return outer.getIdentifierReference( name, strict );
    }

}
