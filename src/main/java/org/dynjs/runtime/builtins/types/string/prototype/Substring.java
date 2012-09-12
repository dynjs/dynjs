package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Substring extends AbstractNativeFunction {

    public Substring(GlobalObject globalObject) {
        super(globalObject, "start", "end");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.15
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        int len = s.length();
        
        long intStart = Types.toInteger( context, args[0] );
        long intEnd = ( args[1] == Types.UNDEFINED ? len : Types.toInteger(context, args[1]));
        
        int finalStart = (int) Math.min( Math.max( intStart, 0 ), len );
        int finalEnd = (int) Math.min( Math.max( intEnd, 0 ), len );
        
        int from = Math.min( finalStart, finalEnd );
        int to = Math.max( finalStart, finalEnd );
        
        return s.substring(from, to);
    }

}
