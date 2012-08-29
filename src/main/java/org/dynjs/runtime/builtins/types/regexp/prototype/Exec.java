package org.dynjs.runtime.builtins.types.regexp.prototype;

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
        String str = Types.toString(args[0]);

        DynRegExp regexp = (DynRegExp) self;
        int flags = 0;

        if (regexp.get(context, "multiline") == Boolean.TRUE) {
            flags = flags | Pattern.MULTILINE;
        }

        if (regexp.get(context, "ignoreCase") == Boolean.TRUE) {
            flags = flags | Pattern.CASE_INSENSITIVE;
        }

        String patternStr = (String) regexp.get(context, "source");
        Pattern pattern = Pattern.compile(patternStr, flags);

        int lastIndex = (int) regexp.get(context, "lastIndex");
        int i = lastIndex;

        if (regexp.get(context, "global") == Boolean.FALSE) {
            i = 0;
        }

        boolean matchSucceeded = false;
        int strLen = str.length();

        Matcher matcher = pattern.matcher(str);
        while (!matchSucceeded) {
            if (i < 0 || i > strLen) {
                regexp.put(context, "lastIndex", 0, true);
                return Types.NULL;
            }
            if (!matcher.find(i)) {
                ++i;
            } else {
                matchSucceeded = true;
            }
        }
        if (regexp.get(context, "global") == Boolean.TRUE) {
            regexp.put(context, "lastIndex", matcher.end(), true);
        }

        DynArray a = BuiltinArray.newArray(context);
        a.put(context, "index", matcher.start(), true);
        a.put(context, "input", str, true);
        a.put(context, "length", matcher.groupCount() + 1, true);
        a.put(context, "0", matcher.group(0), true);
        for (int j = 1; j <= matcher.groupCount(); ++j) {
            a.put(context, "" + j, matcher.group(j), true);
        }

        return a;
    }

}
