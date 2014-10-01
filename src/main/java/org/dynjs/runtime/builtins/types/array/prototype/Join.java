package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Join extends AbstractNonConstructorFunction {

    public Join(GlobalContext globalContext) {
        super(globalContext, "separator");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.5

        JSObject array = Types.toObject(context, self);
        long len = Types.toUint32(context, array.get(context, "length"));

        String separator = ",";
        if (args.length >= 1) {
            if (args[0] != Types.UNDEFINED) {
                separator = Types.toString(context, args[0]);
            }
        }

        if (len == 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder();

        for (long i = 0; i < len; ++i) {
            if (i > 0) {
                buf.append(separator);
            }
            Object v = array.get(context, "" + i);
            if (v == Types.UNDEFINED || v == Types.NULL) {
                v = "";
            } else {
                v = Types.toString(context, v);
            }
            buf.append(v);
        }

        return buf.toString();
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Join.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: join>";
    }

}
