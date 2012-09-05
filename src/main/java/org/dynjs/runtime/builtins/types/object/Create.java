package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Create extends AbstractNativeFunction {

    public Create(GlobalObject globalObject) {
        super(globalObject, "o", "props");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.5
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) o;

        DynObject newObj = new DynObject(context.getGlobalObject());

        newObj.setPrototype(jsObj);

        Object props = args[1];
        if (props != Types.UNDEFINED) {
            JSObject object = (JSObject) context.getGlobalObject().get(context, "Object");
            JSFunction definePropertiesFn = (JSFunction) object.get(context, "defineProperties");
            context.call(definePropertiesFn, Types.UNDEFINED, new Object[] { newObj, props });
        }

        return newObj;
    }
}
