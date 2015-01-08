package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class Trim extends AbstractNativeFunction {

    public Trim(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.20
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self );
        
        return Types.trimString(s);
    }

}
