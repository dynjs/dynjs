package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class Concat extends AbstractNativeFunction {

    public Concat(GlobalObject globalObject) {
        super(globalObject, "string1");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.5
        StringBuffer s = new StringBuffer();
        
        s.append( Types.toString(context, self));
        
        for ( int i = 0 ; i < args.length; ++i ) {
            s.append( Types.toString( context, args[i] ) );
        }
        
        return s.toString();
    }

}
