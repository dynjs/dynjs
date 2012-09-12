package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Slice extends AbstractNativeFunction {

    public Slice(GlobalObject globalObject) {
        super(globalObject, "start", "end");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.12
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        int len = s.length();
        
        long intStart = Types.toInteger( context, args[0] );
        long intEnd = ( args[1] == Types.UNDEFINED ? len : Types.toInteger(context, args[1]));
        
        int from = 0;
        int to = 0;
        
        if ( intStart < 0 ) {
            from = (int) Math.max( len + intStart, 0 );
        } else {
            from = (int) Math.min( intStart, len );
        }
        
        if ( intEnd < 0 ) {
            to = (int) Math.max( len + intEnd, 0 );
        } else {
            to = (int) Math.min( intEnd, len );
        }
        
        return s.substring(from, to);
    }

}
