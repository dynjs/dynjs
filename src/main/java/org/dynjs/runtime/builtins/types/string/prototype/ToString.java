package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.builtins.types.string.DynString;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        if ( self instanceof String ) {
            return self;
        }
        
        if ( ! ( self instanceof DynString ) ) {
            throw new ThrowException(context, context.createTypeError("String.toString() only allowed on strings"));
        }
        
        PrimitiveDynObject str = (PrimitiveDynObject) self;

        return str.getPrimitiveValue();
    }

}
