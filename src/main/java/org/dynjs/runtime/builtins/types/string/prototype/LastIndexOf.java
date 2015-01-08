package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class LastIndexOf extends AbstractNonConstructorFunction {

    public LastIndexOf(GlobalContext globalContext) {
        super(globalContext, "searchString");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.7
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);
        String searchStr = Types.toString(context, args[0]);
        Number numPos = null;
        if (args.length >= 2) {
            numPos = Types.toNumber(context, args[1]);
        } else {
            numPos = s.length();
        }

        long pos = 0;

        if (Double.isNaN(numPos.doubleValue())) {
            pos = Integer.MAX_VALUE;
        } else {
            pos = Types.toInteger(context, numPos);
        }

        int start = (int) Math.min(Math.max(pos, 0), s.length());

        return (long) s.lastIndexOf(searchStr, start);
    }

}
