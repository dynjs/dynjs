package org.dynjs.runtime;

public class Types {
    
    public static boolean sameValue(Object left, Object right) {
        
        if ( left.getClass() != right.getClass() ) {
            return false;
        }
        
        if ( left == DynThreadContext.UNDEFINED ) {
            return true;
        }
        
        if ( left == DynThreadContext.NULL ) {
            return true;
        }
        
        return left.equals(  right  );
    }

}
