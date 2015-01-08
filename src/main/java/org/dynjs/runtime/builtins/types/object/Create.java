package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Create extends AbstractNativeFunction {

    public Create(GlobalContext globalContext) {
        super(globalContext, "o", "props");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.5
        Object o = args[0];

        if (!(o instanceof JSObject) && o != Types.NULL) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        DynObject newObj = new DynObject(context.getGlobalContext());

        if (o != Types.NULL) {
            JSObject jsObj = (JSObject) o;
            newObj.setPrototype(jsObj);
        } else {
            newObj.setPrototype(null);
        }

        Object props = args[1];
        if (props != Types.UNDEFINED) {
            JSObject object = (JSObject) context.getGlobalContext().getObject().get(context, "Object");
            JSFunction definePropertiesFn = (JSFunction) object.get(context, "defineProperties");
            context.call(definePropertiesFn, Types.UNDEFINED, new Object[] { newObj, props });
        }

        return newObj;
    }
}
