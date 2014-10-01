package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;

public class ToString extends AbstractNonConstructorFunction {

    public ToString(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.2
        if (!(self instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("Function.toString() only allowed on functions"));
        }

        return self.toString();
    }

}
