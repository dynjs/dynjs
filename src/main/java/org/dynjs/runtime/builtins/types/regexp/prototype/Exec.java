package org.dynjs.runtime.builtins.types.regexp.prototype;

import java.util.Arrays;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;
import org.dynjs.runtime.builtins.types.regexp.DynRegExpMatch;

public class Exec extends AbstractNonConstructorFunction {

    public Exec(GlobalContext globalContext) {
        super(globalContext, "string");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String str = Types.toString(context, args[0]);

        if (!(self instanceof DynRegExp)) {
            throw new ThrowException(context, context.createTypeError("only applicable to a RegExp"));
        }

        DynRegExp regexp = (DynRegExp) self;

        long lastIndex = (long) Types.toInteger(context, regexp.get(context, "lastIndex"));
        long i = lastIndex;

        if (regexp.get(context, "global") == Boolean.FALSE) {
            i = 0;
        }

        boolean matchSucceeded = false;
        int strLen = str.length();

        DynRegExpMatch[] r = null;

        while (!matchSucceeded) {
            if (i < 0 || i > strLen) {
                regexp.put(context, "lastIndex", 0L, true);
                return Types.NULL;
            }
            r = regexp.match(context, str, (int) i);
            if (r != null) {
                matchSucceeded = true;
            } else {
                i = strLen + 1;
            }
        }
        if (regexp.get(context, "global") == Boolean.TRUE) {
            regexp.put(context, "lastIndex", (long) r[0].end, true);
        }

        JSObject a = BuiltinArray.newArray(context);
        a.put(context, "index", (long) r[0].begin, true);
        a.put(context, "input", str, true);
        a.put(context, "length", (long) r.length, true);
        a.put(context, "0", r[0].matched, true);
        for (int j = 1; j < r.length; ++j) {
            if (r[j].begin >= 0 && r[j].end >= 0) {
                a.put(context, "" + j, r[j].matched, true);
            }
        }

        return a;
    }

}
