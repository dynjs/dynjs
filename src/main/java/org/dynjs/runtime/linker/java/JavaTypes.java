package org.dynjs.runtime.linker.java;

public class JavaTypes {
    
    public static Object coerceTo(Object value, Class<?> type) {
        if ( value == null ) {
            return value;
        }
        if ( type.isAssignableFrom( value.getClass() ) ) {
            return value;
        }
        
        return null;
    }

}
