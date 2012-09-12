package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;

public class Trim extends AbstractNativeFunction {

    public Trim(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.20
        String s = Types.toString(context, self );
        
        return Types.trimString(s);
    }

}
