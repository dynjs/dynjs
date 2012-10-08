package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class ToLocaleUpperCase extends AbstractNativeFunction {

    public ToLocaleUpperCase(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.19
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        return s.toUpperCase();
    }

}
