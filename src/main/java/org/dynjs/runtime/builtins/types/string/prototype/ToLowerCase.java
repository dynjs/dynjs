package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class ToLowerCase extends AbstractNonConstructorFunction {

    public ToLowerCase(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.16
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        return s.toLowerCase();
    }

}
