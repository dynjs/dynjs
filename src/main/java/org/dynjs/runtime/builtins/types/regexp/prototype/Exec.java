package org.dynjs.runtime.builtins.types.regexp.prototype;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

public class Exec extends AbstractNativeFunction {

    public Exec(GlobalObject globalObject) {
        super(globalObject, true, "string");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String str = Types.toString(context, args[0]);

        DynRegExp regexp = (DynRegExp) self;
        int flags = 0;

        if (regexp.get(context, "multiline") == Boolean.TRUE) {
            flags = flags | Pattern.MULTILINE;
        }

        if (regexp.get(context, "ignoreCase") == Boolean.TRUE) {
            flags = flags | Pattern.CASE_INSENSITIVE;
        }

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

        DynArray a = BuiltinArray.newArray(context);
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
