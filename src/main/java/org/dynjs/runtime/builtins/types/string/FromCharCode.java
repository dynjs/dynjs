package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class FromCharCode extends AbstractNativeFunction {

    public FromCharCode(GlobalContext globalContext) {
        super(globalContext, "char0");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.3.2
        
        if ( args[0] == Types.UNDEFINED ) {
            return "";
        }
        
        char[] chars = new char[args.length];

        for (int i = 0; i < args.length; ++i) {
            chars[i] = (char) Types.toUint16(context, args[i]).intValue();
        }

        String result = new String(chars);
        return result;
    }

}
