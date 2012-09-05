package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;

public class Splice extends AbstractNativeFunction {

    public Splice(GlobalObject globalObject) {
        super(globalObject, "start", "deleteCount");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.11
        JSObject o = Types.toObject(context, self);

        int len = Types.toInteger(context, o.get(context, "length"));
        int relativeStart = Types.toInteger(context, args[0]);
        int actualStart = relativeStart;

        if (relativeStart < 0) {
            actualStart = len + relativeStart;
        }

        int deleteCount = Types.toInteger(context, args[1]);
        int actualDeleteCount = Math.min(Math.max(deleteCount, 0), len - actualStart);

        DynArray a = BuiltinArray.newArray(context);

        for (int k = 0; k < actualDeleteCount; ++k) {
            if (o.hasProperty(context, "" + (actualStart + k))) {
                final Object fromValue = o.get(context, "" + (actualStart + k));
                a.defineOwnProperty(context, "" + k, new PropertyDescriptor() {
                    {
                        set("Value", fromValue);
                        set("Writable", true);
                        set("Configurable", true);
                        set("Enumerable", true);
                    }
                }, false);
            }
        }

        int itemCount = args.length - 2;

        if (itemCount < actualDeleteCount) {
            for (int k = actualStart; k < (len - actualDeleteCount); ++k) {
                if (o.hasProperty(context, "" + (k + actualDeleteCount))) {
                    final Object fromValue = o.get(context, "" + (k + actualDeleteCount));
                    o.put(context, "" + (k + itemCount), fromValue, true);
                } else {
                    o.delete(context, "" + (k + itemCount), true);
                }
            }

            for (int k = len; k > (len - actualDeleteCount + itemCount); --k) {
                o.delete(context, "" + (k - 1), true);
            }
        } else if (itemCount > actualDeleteCount) {
            for (int k = (len - actualDeleteCount); k > actualStart; --k) {
                if (o.hasProperty(context, "" + (k + actualDeleteCount - 1))) {
                    final Object fromValue = o.get(context, "" + (k + actualDeleteCount - 1));
                    o.put(context, "" + (k + itemCount - 1), fromValue, true);
                } else {
                    o.delete(context, "" + (k + itemCount - 1), true);
                }
            }
        }

        for (int k = actualStart, i = 0; i < itemCount; ++k, ++i) {
            o.put(context, "" + k, args[i + 2], true);
        }

        return a;
    }

}
