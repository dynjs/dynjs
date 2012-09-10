package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class IndexOf extends AbstractNativeFunction {

    public IndexOf(GlobalObject globalObject) {
        super(globalObject, "searchString");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.7
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        String searchStr = Types.toString( context, args[0] );
        long pos = 0;
        if ( args.length >= 2 ) {
            pos = Types.toInteger(context, args[1]);
        }
        
        int start = (int) Math.min( Math.max(pos, 0), s.length() );
        
        return (long) s.indexOf(searchStr, start );
    }

}
