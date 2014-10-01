package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class LastIndexOf extends AbstractNativeFunction {

    public LastIndexOf(GlobalContext globalContext) {
        super(globalContext, "searchElement");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.14
        JSObject o = Types.toObject(context, self);
        long len = Types.toUint32(context, o.get(context, "length"));

        if (len == 0) {
            return -1L;
        }

        long n = len;
        if (args.length >= 2) {
            if (args[1] != Types.UNDEFINED) {
                n = Types.toInteger(context, args[1]);
            } else {
                n = (long)Double.NaN;  // http://es5.github.com/rev1/x15.4.4.15.html
            }
        }

        long k = Math.min(n, len-1);
        if (n < 0) {
            k = (len - Math.abs(n));
        }

        while ( k >= 0 ) {
            if (o.hasProperty(context, "" +k )) {
                Object element = o.get(context, ""+k);
                if ( Types.compareStrictEquality(context, args[0], element)) {
                    return k;
                }
            }
            --k;
        }
        
        return -1L;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/LastIndexOf.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: lastIndexOf>";
    }

}
