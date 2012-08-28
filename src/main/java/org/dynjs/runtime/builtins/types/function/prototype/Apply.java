package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Apply extends AbstractNativeFunction {

    public Apply(GlobalObject globalObject) {
        super(globalObject, "thisArg", "argArray" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.3
        if ( ! ( self instanceof JSFunction ) ) {
            throw new ThrowException( context.createTypeError( "Function.apply() only allowed on callable objects" ));
        }
        
        Object thisArg = args[0];
        Object argArray = args[1];
        
        if ( argArray == Types.UNDEFINED || argArray == Types.NULL ) {
            
        }
        
        if ( ! Types.type( argArray ).equals( "object" ) ) {
            throw new ThrowException( context.createTypeError( "argArray must be an object" ));
        }
        
        int len = Types.toUint32( ((JSObject)argArray).get( context, "length" ) );
        Object[] argList = new Object[len];
        
        for ( int i = 0 ; i < len ; ++i ) {
            argList[i] = ((JSObject)argArray).get( context, "" + i );
        }
        
        return context.call((JSFunction) self, thisArg, argList );
    }

}
