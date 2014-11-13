package org.dynjs.runtime.builtins.types.number.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.number.DynNumber;

public class ValueOf extends AbstractNativeFunction {
    public ValueOf(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.7.4.4
        if (self instanceof DynNumber) {
            return ((DynNumber) self).getPrimitiveValue();
        }
        if ( self instanceof Number ) {
            return self;
        }
        throw new ThrowException( context, context.createTypeError( "Number.valueOf() only allowed on Numbers" ));
    }
}
