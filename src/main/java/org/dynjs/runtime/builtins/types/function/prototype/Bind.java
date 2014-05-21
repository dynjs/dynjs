package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.BoundFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;

public class Bind extends AbstractNativeFunction {
    public static final Object[] EMPTY_ARRAY = new Object[0];

    public Bind(GlobalObject globalObject) {
        super(globalObject, "thisArg");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.5
        if (!(self instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("Function.bind() only allowed on callable objects"));
        }
        

        JSFunction target = (JSFunction) self;
        
        Object thisArg = args[0];
        Object[] argValues;

        if (args.length > 1) {
            argValues = new Object[args.length - 1];
            for (int i = 0; i < args.length - 1; ++i) {
                argValues[i] = args[i + 1];
            }
        } else {
            argValues = EMPTY_ARRAY;
        }

        return new BoundFunction(context.getGlobalObject(), getScope(), target, thisArg, argValues);
    }

}
