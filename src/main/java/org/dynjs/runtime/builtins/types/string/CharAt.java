package org.dynjs.runtime.builtins.types.string;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class CharAt extends AbstractNativeFunction {

    public CharAt(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        String strSelf = Types.toString(context, self);
        int position = Types.toInteger(context, args[0]);
        if (position < 0 || position > strSelf.length()) {
            return "";
        }

        return "" + strSelf.charAt(position);
    }

}
