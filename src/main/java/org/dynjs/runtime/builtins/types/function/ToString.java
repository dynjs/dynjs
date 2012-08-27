package org.dynjs.runtime.builtins.types.function;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;

public class ToString extends AbstractNativeFunction {

    public ToString(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        if ( ! ( self instanceof JSFunction ) ) {
            throw new ThrowException( context.createTypeError( "Function.toString() only allowed on functions" ));
        }
        
        return self.toString();
    }

}
