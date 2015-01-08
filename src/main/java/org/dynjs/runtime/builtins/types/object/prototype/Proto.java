package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;

public class Proto extends AbstractNativeFunction {

    public Proto(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if (!(self instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) self;
        if (args.length == 1) {
            if (!jsObj.isExtensible()) {
                throw new ThrowException(context, context.createTypeError("must be extenible"));
            }
            if (args[0] instanceof JSObject) {
                JSObject newProto = (JSObject)args[0];
                jsObj.setPrototype(newProto);
            }
        }

        return jsObj.getPrototype();
    }

}
