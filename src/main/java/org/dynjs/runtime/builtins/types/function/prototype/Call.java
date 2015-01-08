package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.util.CallHelpers;

public class Call extends AbstractNonConstructorFunction {

    public Call(GlobalContext globalContext) {
        super(globalContext, "thisArg");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.4
        if (!(self instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("Function.call() only allowed on callable objects"));
        }

        Object thisArg = args[0];
        Object[] argList = CallHelpers.allButFirstArgument(args);

        return context.call((JSFunction) self, thisArg, argList);
    }

}
