package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class LocaleCompare extends AbstractNonConstructorFunction {

    public LocaleCompare(GlobalContext globalContext) {
        super(globalContext, "that");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.9
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        String that = Types.toString(context, args[0] );
        
        return (long) s.compareTo( that );
    }
}
