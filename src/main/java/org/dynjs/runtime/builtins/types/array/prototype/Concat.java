package org.dynjs.runtime.builtins.types.array.prototype;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class Concat extends AbstractNativeFunction {

    public Concat(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.4
        JSObject o = Types.toObject(context, self);

        DynArray array = new DynArray(context.getGlobalObject());

        List<Object> items = new ArrayList<>();
        items.add(o);

        for (int i = 0; i < args.length; ++i) {
            items.add(args[i]);
        }

        int n = 0;
        for (Object e : items) {
            if (e instanceof JSObject && ((JSObject) e).getClassName().equals("Array")) {
                JSObject jsE = (JSObject) e;

                long len = Types.toInteger(context, jsE.get(context, "length"));

                for (long k = 0; k < len; ++k) {
                    if (jsE.hasProperty(context, "" + k)) {
                        final Object subElement = jsE.get(context, "" + k);
                        array.defineOwnProperty(context, "" + n, new PropertyDescriptor() {
                            {
                                set( "Value", subElement);
                                set( "Writable", true);
                                set( "Configurable", true);
                                set( "Enumerable", true);
                            }
                        }, false);
                    }
                    ++n;
                }
            } else {
                final Object finalE = e;
                array.defineOwnProperty(context, "" + n, new PropertyDescriptor() {
                    {
                        set( "Value", finalE);
                        set( "Writable", true);
                        set( "Configurable", true);
                        set( "Enumerable", true);
                    }
                }, false);
                ++n;
            }
        }

        return array;
    }
}
