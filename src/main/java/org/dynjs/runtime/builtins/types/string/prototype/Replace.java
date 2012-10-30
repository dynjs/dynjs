package org.dynjs.runtime.builtins.types.string.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
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
        DynRegExp regExp    = null;
        DynArray matches    = null;
        String original     = Types.toString(context, self);
        String result       = original;        
        if (args.length >= 2) {
            if (args[0] instanceof DynRegExp) {
                regExp = (DynRegExp) args[0];
                Match match = new Match(context.getGlobalObject());
                matches = (DynArray) match.call(context, original, regExp);
                for ( int i = 0 ; i < Types.toInteger(context, matches.get(context, "length")); ++i ) {
                    String nextMatch = Types.toString(context, matches.get(context, ""+i));
                    String newString = "";                    
                    if (args[1] instanceof JSFunction) {
                        Object[] functionArgs = {nextMatch};
                        newString = (String) context.call( (JSFunction)args[1], null, functionArgs);
                    } else {
                        // TODO: Handle text symbol substitution on newstring
                        newString = Types.toString(context, args[1]);
                    }
                    result = result.replaceFirst(nextMatch, newString);
                }
            } else {
                String searchString = Types.toString(context, args[0]);
                result = original.replaceFirst(searchString, Types.toString(context, args[1]));
            }
            
        }
        return result;
    }

}
