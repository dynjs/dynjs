package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

public class Replace extends AbstractNativeFunction {

    public Replace(GlobalObject globalObject) {
        super(globalObject, "searchValue", "replaceValue");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Types.checkObjectCoercible(context, self);
        String original = Types.toString(context, self);
        String result = original;
        if (args.length == 2) {
            if (args[0] instanceof DynRegExp) {
                System.err.println("We will deal with this eventually");
            } else {
                String searchString = Types.toString(context, args[0]);
                if (args[1] instanceof JSFunction) {
                    // Second arg was a function - let's deal with that later
                } else {
                    String newString = Types.toString(context, args[1]);
                    result = original.replaceAll(searchString, newString);
                }
            }
        }
        return result;
    }

}
