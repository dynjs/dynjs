package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;

public class Call extends AbstractNativeFunction {

    public Call(GlobalObject globalObject) {
        super(globalObject, "thisArg" );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.4
        if ( ! ( self instanceof JSFunction ) ) {
            throw new ThrowException( context.createTypeError( "Function.call() only allowed on callable objects" ));
        }
        
        Object thisArg = args[0];
        Object[] argList = null;
        if ( args.length > 1 ) {
            argList = new Object[ args.length - 1 ];
            for ( int i = 0 ; i < argList.length ; ++i ) {
                argList[i] = args[i+1];
            }
        } else {
            argList = new Object[0];
        }
        
        return context.call((JSFunction) self, thisArg, argList );
    }

}
