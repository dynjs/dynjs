package org.dynjs.runtime.builtins.types.string.prototype;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                // We can reuse the global String.prototype.match function - let's look it up and call it
                JSFunction fn = (JSFunction) context.getGlobalObject().getPrototypeFor( "String" ).get( context, "match" );
                matches = (DynArray) context.call(fn, original, regExp);
                for ( int i = 0 ; i < Types.toInteger(context, matches.get(context, "length")); ++i ) {
                    String nextMatch = Types.toString(context, matches.get(context, ""+i));
                    result = result.replaceFirst(nextMatch, replacementString(context, nextMatch, replaceWith, matches));
                }
            } else {
                String searchString = Types.toString(context, args[0]);
                String match = replacementString(context, searchString, replaceWith, null);
                result = original.replaceFirst(searchString, match);
            }
            
        }
        return result;
    }

    protected String replacementString(ExecutionContext context, String nextMatch, final Object replaceWith, DynArray matches) {
        String newString = "";
        if (replaceWith instanceof JSFunction) {
            Object[] functionArgs = {nextMatch};
            newString = (String) context.call( (JSFunction)replaceWith, null, functionArgs);
        } else {
            // TODO: Handle $` and $' substitution on newstring            
            newString = Types.toString(context, replaceWith);
            newString = newString.replaceAll("\\$\\$", Matcher.quoteReplacement("\\$"));
            if (matches != null) {
                newString = newString.replaceAll("\\$&", Matcher.quoteReplacement((String) matches.get(context, "0")));
                // Deal with $n string substitution
                Pattern pattern = Pattern.compile("\\$(\\d+)");
                Matcher matcher = pattern.matcher(newString);
                if (matcher.matches()) {
                    final String group = matcher.group(1);
                    newString = newString.replaceAll("\\$"+group, Matcher.quoteReplacement((String) matches.get(context, group)));
                }
            }
        }
        return newString;
    }

}
