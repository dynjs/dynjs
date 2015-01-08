package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;

public class Splice extends AbstractNonConstructorFunction {

    public Splice(GlobalContext globalContext) {
        super(globalContext, "start", "deleteCount");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.12
        JSObject o = Types.toObject(context, self);

        long len = Types.toUint32(context, o.get(context, "length"));
        long relativeStart = Types.toInteger(context, args[0]);
        long actualStart = 0;

        if (relativeStart < 0) {
            actualStart = Math.max( len + relativeStart, 0 );
        } else {
            actualStart = Math.min( relativeStart, len );
        }

        long deleteCount = Types.toInteger(context, args[1]);
        long actualDeleteCount = Math.min(Math.max(deleteCount, 0), len - actualStart);

        JSObject a = BuiltinArray.newArray(context);

        for (long k = 0; k < actualDeleteCount; ++k) {
            if (o.hasProperty(context, "" + (actualStart + k))) {
                final Object fromValue = o.get(context, "" + (actualStart + k));
                a.defineOwnProperty(context, "" + k,
                        PropertyDescriptor.newDataPropertyDescriptor(fromValue, true, true, true), false);
            }
        }

        long itemCount = args.length - 2;

        if (itemCount < actualDeleteCount) {
            for (long k = actualStart; k < (len - actualDeleteCount); ++k) {
                if (o.hasProperty(context, "" + (k + actualDeleteCount))) {
                    final Object fromValue = o.get(context, "" + (k + actualDeleteCount));
                    o.put(context, "" + (k + itemCount), fromValue, true);
                } else {
                    o.delete(context, "" + (k + itemCount), true);
                }
            }

            for (long k = len; k > (len - actualDeleteCount + itemCount); --k) {
                o.delete(context, "" + (k - 1), true);
            }
        } else if (itemCount > actualDeleteCount) {
            for (long k = (len - actualDeleteCount); k > actualStart; --k) {
                if (o.hasProperty(context, "" + (k + actualDeleteCount - 1))) {
                    final Object fromValue = o.get(context, "" + (k + actualDeleteCount - 1));
                    o.put(context, "" + (k + itemCount - 1), fromValue, true);
                } else {
                    o.delete(context, "" + (k + itemCount - 1), true);
                }
            }
        }

        for (long k = actualStart, i = 0; i < itemCount; ++k, ++i) {
            o.put(context, "" + k, args[ (int) i + 2], true);
        }
        
        o.put(context, "length", (len-actualDeleteCount + itemCount), true);

        return a;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Splice.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: splice>";
    }

}
