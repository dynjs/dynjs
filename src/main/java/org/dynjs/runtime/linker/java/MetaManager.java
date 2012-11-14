package org.dynjs.runtime.linker.java;

import java.util.HashMap;
import java.util.Map;

public class MetaManager {
    
    private Map<Class<?>, ClassMeta> classMeta = new HashMap<>();
    
    public MetaManager() {
        
    }
    
    public ClassMeta getMetaFor(Class<?> clazz) {
        ClassMeta meta = this.classMeta.get(clazz);
        if ( meta == null ) {
            meta = createMetaFor(clazz);
            this.classMeta.put( clazz, meta );
        }
        
        return meta;
    }

    private ClassMeta createMetaFor(Class<?> clazz) {
        return new ClassMeta(clazz);
    }

}
