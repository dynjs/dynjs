package org.dynjs.runtime.builtins.types.regexp.prototype;

import java.util.regex.MatchResult;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractPrototypeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

public class Exec extends AbstractPrototypeFunction {

    public Exec(GlobalObject globalObject) {
        super(globalObject, "string");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String str = Types.toString(context, args[0]);

        if (!(self instanceof DynRegExp)) {
            throw new ThrowException(context, context.createTypeError("only applicable to a RegExp"));
        }

        DynRegExp regexp = (DynRegExp) self;

        long lastIndex = (long) regexp.get(context, "lastIndex");
        long i = lastIndex;

        if (regexp.get(context, "global") == Boolean.FALSE) {
            i = 0;
        }

        boolean matchSucceeded = false;
        int strLen = str.length();

        MatchResult r = null;

        while (!matchSucceeded) {
            if (i < 0 || i > strLen) {
                regexp.put(context, "lastIndex", 0L, true);
                return Types.NULL;
            }
            r = regexp.match(str, (int) i);
            if (r != null) {
                matchSucceeded = true;
            } else {
                ++i;
            }
        }
        if (regexp.get(context, "global") == Boolean.TRUE) {
            regexp.put(context, "lastIndex", (long) r.end(), true);
        }

        JSObject a = BuiltinArray.newArray(context);
        a.put(context, "index", (long) r.start(), true);
        a.put(context, "input", str, true);
        a.put(context, "length", (long) r.groupCount() + 1, true);
        a.put(context, "0", r.group(0), true);
        for (int j = 1; j <= r.groupCount(); ++j) {
            a.put(context, "" + j, r.group(j), true);
        }

        return a;
    }

}
