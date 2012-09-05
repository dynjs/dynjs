package org.dynjs.runtime.builtins.types.array;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

public class IsArray extends AbstractNativeFunction {

    public IsArray(GlobalObject globalObject) {
        super(globalObject, true, "arg");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.3.2
        return (args[0] instanceof DynArray);
    }

}
