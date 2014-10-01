package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Apply extends AbstractNonConstructorFunction {

    public Apply(GlobalContext globalContext) {
        super(globalContext, "thisArg", "argArray");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.3
        
        if (!(self instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("Function.apply() only allowed on callable objects"));
        }

        Object thisArg = args[0];
        Object argArray = args[1];

        if (argArray == Types.UNDEFINED || argArray == Types.NULL) {
            return context.call((JSFunction) self, thisArg);
        }
        
        if (! ( argArray instanceof JSObject ) ) {
            throw new ThrowException(context, context.createTypeError("argArray must be an object"));
        }

        long len = Types.toUint32(context, ((JSObject) argArray).get(context, "length"));
        Object[] argList = new Object[(int)len];

        for (int i = 0; i < len; ++i) {
            argList[i] = ((JSObject) argArray).get(context, "" + i);
        }

        return context.call((JSFunction) self, thisArg, argList);
    }

}
