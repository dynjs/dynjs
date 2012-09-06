package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class IndexOf extends AbstractNativeFunction {

    public IndexOf(GlobalObject globalObject) {
        super(globalObject, "searchElement");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.14
        JSObject o = Types.toObject(context, self);
        int len = Types.toInteger(context, o.get(context, "length"));

        if (len == 0) {
            return -1;
        }

        int n = 0;
        if (args.length >= 2) {
            if (args[1] != Types.UNDEFINED) {
                n = Types.toInteger(context, args[1]);
            }
        }

        if (n >= len) {
            return -1;
        }

        int k = n;
        if (n < 0) {
            k = (len - Math.abs(n));
            if ( k < 0 ) {
                k = 0;
            }
        }

        while ( k < len ) {
            if (o.hasProperty(context, "" +k )) {
                Object element = o.get(context, ""+k);
                if ( Types.compareStrictEquality(context, args[0], element)) {
                    return k;
                }
            }
            ++k;
        }
        
        return -1;
    }

}
