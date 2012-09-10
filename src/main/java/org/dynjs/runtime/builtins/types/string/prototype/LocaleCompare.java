package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class LocaleCompare extends AbstractNativeFunction {

    public LocaleCompare(GlobalObject globalObject) {
        super(globalObject, "that");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.9
        String s = Types.toString(context, self);
        String that = Types.toString(context, args[0] );
        
        return (long) s.compareTo( that );
    }
}
