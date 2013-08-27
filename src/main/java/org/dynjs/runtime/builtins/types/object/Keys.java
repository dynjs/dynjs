package org.dynjs.runtime.builtins.types.object;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
import org.dynjs.runtime.builtins.types.BuiltinArray;

public class Keys extends AbstractNativeFunction {

    public Keys(GlobalObject globalObject) {
        super(globalObject, "o");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.3.14
        Object o = args[0];

        if (!(o instanceof JSObject)) {
            throw new ThrowException(context, context.createTypeError("must be an object"));
        }

        JSObject jsObj = (JSObject) o;

        JSObject array = BuiltinArray.newArray(context);

        int i = 0;
        NameEnumerator names = jsObj.getOwnEnumerablePropertyNames();
        while (names.hasNext()) {
            final String name = names.next();
            PropertyDescriptor desc = new PropertyDescriptor();
            desc.set(Names.VALUE, name);
            desc.set(Names.WRITABLE, true);
            desc.set(Names.CONFIGURABLE, true);
            desc.set(Names.ENUMERABLE, true);
            array.defineOwnProperty(context, "" + i, desc, false);
            ++i;
        }
        return array;
    }
}
