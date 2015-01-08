package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class GetOwnPropertyDescriptor extends AbstractNativeFunction {

    public GetOwnPropertyDescriptor(GlobalContext globalContext) {
        super(globalContext, "o", "p");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.3
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) o;

        String name = Types.toString(context, args[1]);

        Object desc = jsObj.getOwnProperty(context, name, false);
        
        //System.err.println( jsObj + " get: " + name + " > " + desc );

        return PropertyDescriptor.fromPropertyDescriptor(context, desc);
    }
}
