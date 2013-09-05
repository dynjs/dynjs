package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class CharCodeAt extends AbstractNonConstructorFunction {

    public CharCodeAt(GlobalObject globalObject) {
        super(globalObject, "pos");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.5
        Types.checkObjectCoercible(context, self);
        String strSelf = Types.toString(context, self);
        long position = Types.toInteger(context, args[0]);
        if (position < 0 || position >= strSelf.length()) {
            return Double.NaN;
        }

        return (long) strSelf.charAt( (int) position);
    }

}
