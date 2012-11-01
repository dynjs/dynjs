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
        // We can reuse the global String.prototype.match function to match the regexp
        JSFunction matchFunction = (JSFunction) context.getGlobalObject().getPrototypeFor( "String" ).get( context, "match" );
        String string = Types.UNDEFINED.toString();        
        
        if (args.length == 2) {
            Object searchValue  = args[0];
            Object replaceValue = args[1];
            DynArray matches    = null;
            boolean globalFlag  = false;
            
            Types.checkObjectCoercible(context, self);
            string = Types.toString(context, self);
            
            int m = 0;
            if (searchValue instanceof DynRegExp) {
                DynRegExp regExp = (DynRegExp) searchValue;
                matches = (DynArray) context.call(matchFunction, string, regExp);
                Pattern pattern = Pattern.compile(".*\\(.+\\).*");
                Matcher matcher = pattern.matcher(Types.toString(context, searchValue));
                m = matcher.groupCount();
            } else {
                String searchString = Types.toString(context, searchValue);
                matches = new DynArray(context.getGlobalObject());
                matches.put(context, "0", searchString, false);
            }
            
            Long matchCount = Types.toInteger(context, matches.get(context, "length"));
            if (replaceValue instanceof JSFunction) {
                for ( int i = 0 ; i < matchCount; ++i ) {
                    String nextMatch = Types.toString(context, matches.get(context, ""+i));
                    // TODO: Include MatchResult captures and match index in function args
                    Object[] functionArgs = {matches.get(context, ""+i), Types.UNDEFINED, 0, string};
                    String replacement = (String) context.call( (JSFunction)replaceValue, null, functionArgs);
                    string = string.replaceFirst(nextMatch, Matcher.quoteReplacement(replacement));
                }
            } else {
                String newString = Types.toString(context, replaceValue);
                for ( int i = 1; i <= matchCount; ++i ) {
                    string = string.replaceFirst((String)matches.get(context, "0"), Matcher.quoteReplacement(buildReplacementString(context, newString, matches, i)));
                }
            }
        }
        return string;
    }

    protected String buildReplacementString(ExecutionContext context, String replaceWith, DynArray matches, int matchIndex) {
        // TODO: Handle $` and $' substitution on newstring            
        replaceWith = replaceWith.replaceAll("\\$\\$", Matcher.quoteReplacement("$"));
        replaceWith = replaceWith.replaceAll("\\$&", Matcher.quoteReplacement((String) matches.get(context, "0")));
        // Deal with $n string substitution
        Pattern pattern = Pattern.compile("(?:.*(?:\\$(\\d+)).*)");
        Matcher matcher = pattern.matcher(replaceWith);
        while (matcher.matches()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                final String group = matcher.group(i);
                replaceWith = replaceWith.replaceAll("\\$"+group, Matcher.quoteReplacement((String) matches.get(context, group)));
                matcher = pattern.matcher(replaceWith);
            }
        }
        return replaceWith;
    }

}
