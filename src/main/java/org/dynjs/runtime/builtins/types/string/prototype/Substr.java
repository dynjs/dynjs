package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class Substr extends AbstractNativeFunction {
    
    public Substr(GlobalContext globalContext) {
        super(globalContext, "start", "length");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // http://es5.github.com/#B.2.3
        String s = Types.toString(context, self);
        int start = Types.toInt32(context, args[0]).intValue();
        int length = (int) Double.POSITIVE_INFINITY;
        if (args[1] != Types.UNDEFINED) {
            length = Types.toInt32(context, args[1]).intValue();
        }
        int chars = s.length();
        if (start < 0) {
            start = Math.max(chars+start, 0);
        }
        length = Math.min(Math.max(length, 0), chars-start);
        if (length <= 0) return "";
        return s.substring(start, start+length);
    }

}
