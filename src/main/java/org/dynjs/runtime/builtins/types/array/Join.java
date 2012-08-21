package org.dynjs.runtime.builtins.types.array;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Join extends AbstractNativeFunction {

    public Join(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.5

        JSObject array = Types.toObject(self);
        int len = Types.toUint32(array.get(context, "length"));
        if (len == 0) {
            return "";
        }

        String separator = ",";
        if (args.length >= 1) {
            if (args[0] != Types.UNDEFINED) {
                separator = Types.toString(args[0]);
            }
        }

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < len; ++i) {
            if (i > 0) {
                buf.append(separator);
            }
            Object v = array.get(context, "" + i);
            System.err.println(i + "V=" + v);
            if (v == Types.UNDEFINED || v == Types.NULL) {
                v = "";
            } else {
                v = Types.toString(v);
            }
            buf.append(v);
        }

        return buf.toString();
    }

}
