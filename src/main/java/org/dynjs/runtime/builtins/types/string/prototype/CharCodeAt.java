package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class CharCodeAt extends AbstractNativeFunction {

    public CharCodeAt(GlobalObject globalObject) {
        super(globalObject, "pos");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.5
        String strSelf = Types.toString(context, self);
        int position = Types.toInteger(context, args[0]);
        if (position < 0 || position > strSelf.length()) {
            return "";
        }

        return (int) strSelf.charAt(position);
    }

}
