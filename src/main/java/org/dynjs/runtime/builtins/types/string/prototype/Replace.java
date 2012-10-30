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
            final Object replaceWith = args[1];
            if (args[0] instanceof DynRegExp) {
                regExp = (DynRegExp) args[0];
                JSFunction fn = (JSFunction) context.getGlobalObject().getPrototypeFor( "String" ).get( context, "match" );
                matches = (DynArray) context.call(fn, original, regExp);
                for ( int i = 0 ; i < Types.toInteger(context, matches.get(context, "length")); ++i ) {
                    String nextMatch = Types.toString(context, matches.get(context, ""+i));
                    result = result.replaceFirst(nextMatch, replaceMatch(context, nextMatch, replaceWith));
                }
            } else {
                String searchString = Types.toString(context, args[0]);
                String match = replaceMatch(context, searchString, replaceWith);
                result = original.replaceFirst(searchString, match);
            }
            
        }
        return result;
    }

    protected String replaceMatch(ExecutionContext context, String nextMatch, final Object replaceWith) {
        String newString = "";
        if (replaceWith instanceof JSFunction) {
            Object[] functionArgs = {nextMatch};
            newString = (String) context.call( (JSFunction)replaceWith, null, functionArgs);
        } else {
            // TODO: Handle text symbol substitution on newstring
            newString = Types.toString(context, replaceWith);
        }
        return newString;
    }

}
