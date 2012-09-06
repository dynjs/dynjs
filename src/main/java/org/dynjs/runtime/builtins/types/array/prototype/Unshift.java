package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Unshift extends AbstractNativeFunction {

    public Unshift(GlobalObject globalObject) {
        super(globalObject, "item1");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.9
        JSObject o = Types.toObject(context, self);
        long len = Types.toUint32(context, o.get(context, "length"));

        for (long k = len; k > 0; --k) {
            if (o.hasProperty(context, "" + (k - 1))) {
                final Object fromValue = o.get(context, "" + (k - 1));
                o.put(context, "" + (k + args.length - 1), fromValue, true);
            } else {
                o.delete(context, "" + (k + args.length - 1), true);
            }
        }

        for (int j = 0; j < args.length; ++j) {
            o.put(context, "" + j, args[j], true);
        }
        
        o.put( context, "length", len + args.length, true );
        
        return len + args.length;
    }

}
