package org.dynjs.runtime.builtins.types.string;

import org.dynjs.exception.TypeError;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        PrimitiveDynObject str = (PrimitiveDynObject) self;
        if ( ! str.getClassName().equals( "String" ) ) {
            throw new TypeError();
        }
        
        return str.getPrimitiveValue();
    }

}
