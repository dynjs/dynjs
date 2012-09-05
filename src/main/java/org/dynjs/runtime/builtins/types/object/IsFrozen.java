package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class IsFrozen extends AbstractNativeFunction {

    public IsFrozen(GlobalObject globalObject) {
        super(globalObject, "o");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.12
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) o;

        NameEnumerator names = jsObj.getOwnPropertyNames();

        while (names.hasNext()) {
            String name = names.next();
            Object d = jsObj.getOwnProperty(context, name);
            if (d != Types.UNDEFINED) {
                PropertyDescriptor desc = (PropertyDescriptor) d;
                if (desc.isDataDescriptor()) {
                    Object isWritable = desc.get("Writable");
                    if (isWritable == Boolean.TRUE) {
                        return false;
                    }
                }
                Object isConfigurable = desc.get("Configurable");
                if (isConfigurable == Boolean.TRUE) {
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
