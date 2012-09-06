package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class FromCharCode extends AbstractNativeFunction {

    public FromCharCode(GlobalObject globalObject) {
        super(globalObject, "char0");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.3.2
        
        char[] chars = new char[ args.length ];
        
        for ( int i = 0 ; i < args.length ; ++i ) {
            chars[i] = (char) Types.toUint16(context, args[i]).intValue();
        }
        
        return new String(chars);
    }

}
