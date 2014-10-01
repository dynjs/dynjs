package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class IsFrozen extends AbstractNativeFunction {

    public IsFrozen(GlobalContext globalContext) {
        super(globalContext, "o");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.12
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) o;

        NameEnumerator names = jsObj.getOwnPropertyNames();

        while (names.hasNext()) {
            String name = names.next();
            Object d = jsObj.getOwnProperty(context, name, false);
            if (d != Types.UNDEFINED) {
                PropertyDescriptor desc = (PropertyDescriptor) d;
                if (desc.isDataDescriptor() && desc.isWritable()) {
                    return false;
                }
                if (desc.isConfigurable()) {
                    return false;
                }
            }
        }

        if (!jsObj.isExtensible()) {
            return true;
        }

        return false;

    }
}
