package org.dynjs.runtime.java;

import org.dynjs.api.Function;
import org.dynjs.runtime.DynObject;

public class JavaEmbedUtils {

    public static Object invokeProperty(DynObject self, DynThreadContext context, String name, Object... args) {

        System.err.println( "self: " + self );
        System.err.println( "name: " + name );

        DynProperty functionProperty = self.getProperty( name );
        DynObject functionObject = (DynObject) functionProperty.getAttribute( "value" );
        
        return invoke( functionObject, context, args );
    }
    
    public static Object invoke(DynObject self, DynThreadContext context, Object... args) {
        DynProperty callProperty = self.getProperty( "call" );
        Function function = (Function) callProperty.getAttribute( "value" );

        if (args == null) {
            return function.call( self, context );
        } else {
            return function.call( self, context, args );
        }
    }

}
