package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.Arguments;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Unshift extends AbstractNonConstructorFunction {

    public Unshift(GlobalContext globalContext) {
        super(globalContext, "item1");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.9
        JSObject o = Types.toObject(context, self);
        long len = Types.toUint32(context, o.get(context, "length"));
        
        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");

        for (long k = len; k > 0; --k) {
            if (o.hasProperty(context, "" + (k - 1))) {
                final Object fromValue = o.get(context, "" + (k - 1));
                o.put(context, "" + (k + numArgs - 1), fromValue, true);
            } else {
                o.delete(context, "" + (k + numArgs - 1), true);
            }
        }
        
        for (int j = 0; j < numArgs; ++j) {
            o.put(context, "" + j, args[j], true);
        }
        
        o.put( context, "length", len + numArgs, true );
        
        return len + numArgs;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Unshift.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: unshift>";
    }

}
