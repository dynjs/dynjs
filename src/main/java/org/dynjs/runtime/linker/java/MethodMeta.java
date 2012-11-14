package org.dynjs.runtime.linker.java;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

public class MethodMeta {
    
    private String name;
    private List<MethodHandle> methods = new ArrayList<>();

    public MethodMeta(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    void addMethod(MethodHandle method) {
        this.methods.add( method );
    }
    
    public MethodHandle findMethod(Class<?>[] types) {
        for ( MethodHandle each : methods ) {
            if ( matches( each.type().dropParameterTypes(0, 1), types ) ) {
                return each;
            }
        }
        
        return null;
    }
    
    protected boolean matches(MethodType candidate, Class<?>[] target) {
        if (candidate.parameterCount() == target.length) {
            return true;
        }
        return false;
    }
    

}
