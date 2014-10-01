package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.BoundFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.util.CallHelpers;

public class Bind extends AbstractNativeFunction {

    public Bind(GlobalContext globalContext) {
        super(globalContext, "thisArg");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.5
        if (!(self instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("Function.bind() only allowed on callable objects"));
        }
        
        JSFunction target = (JSFunction) self;
        Object thisArg = args[0];
        Object[] argValues = CallHelpers.allButFirstArgument(args);

        return new BoundFunction(context.getGlobalContext(), getScope(), target, thisArg, argValues);
    }

}
