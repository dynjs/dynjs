package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;
import org.joni.Region;

public class Search extends AbstractNonConstructorFunction {

    public Search(GlobalObject globalObject) {
        super(globalObject, "regexp");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.5.4.12
        Types.checkObjectCoercible(context, self);
        String s = Types.toString(context, self);

        JSObject rx = null;
        if (args[0] instanceof JSObject && ((JSObject) args[0]).getClassName().equals("RegExp")) {
            rx = (JSObject) args[0];
        } else {
            rx = BuiltinRegExp.newRegExp(context, args[0] == Types.UNDEFINED ? Types.UNDEFINED : Types.toString(context, args[0]), null);
        }

        Region result = ((DynRegExp) rx).match(context, s, 0);
        if (result == null) {
            return -1L;
        }

        return (long) result.beg[0];
    }

}
