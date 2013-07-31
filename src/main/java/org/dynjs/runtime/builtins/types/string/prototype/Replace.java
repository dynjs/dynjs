package org.dynjs.runtime.builtins.types.string.prototype;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

public class Replace extends AbstractNativeFunction {

    public Replace(GlobalObject globalObject) {
        super(globalObject, "searchValue", "replaceValue");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        JSFunction execFn = (JSFunction) context.getPrototypeFor("RegExp").get(context, "exec");
        String string = Types.NULL.toString();

        if (args.length == 2) {
            Object searchValue = args[0];
            Object replaceValue = args[1];
            Object global      = Boolean.FALSE;
            DynArray matches = new DynArray(context.getGlobalObject());

            Types.checkObjectCoercible(context, self);
            string = Types.toString(context, self);

            int m = 0;
            if (searchValue instanceof DynRegExp) {
                DynRegExp regExp = (DynRegExp) searchValue;

                global = regExp.get(context, "global");
                if (global != Boolean.TRUE) {
                    Object object = context.call(execFn, regExp, string);
                    if (object == Types.NULL || object == Types.UNDEFINED) { // It was not a match
                        matches.put(context, "length", 0L, false);
                    } else {
                        matches.put(context, "length", 1L, false);
                        matches.put(context, "0", object, false);
                        m = Types.toInteger(context, ((JSObject)object).get(context, "length")).intValue()-1;
                    }
                } else {
                    regExp.put(context, "lastIndex", 0L, false);
                    long previousLastIndex = 0;
                    long n = 0;
                    boolean lastMatch = true;

                    while (lastMatch) {
                        final Object result = context.call(execFn, regExp, string);
                        if (result == Types.NULL) {
                            lastMatch = false;
                        } else {
                            long thisIndex = (long) regExp.get(context, "lastIndex");
                            if (thisIndex == previousLastIndex) {
                                regExp.put(context, "lastIndex", thisIndex + 1, false);
                                previousLastIndex = thisIndex + 1;
                            } else {
                                previousLastIndex = thisIndex;
                            }

                            m += Types.toInteger(context, ((JSObject)result).get(context, "length")).intValue()-1;
                            matches.put(context, "" + n, result, false);

                            ++n;
                        }
                        matches.put(context, "length", n, false);
                    }
                    m = Types.toInteger(context, matches.get(context, "length")).intValue() - 1;
                }
            } else {
                final String searchString = Types.toString(context, searchValue);
                long index = string.indexOf(searchString);
                if (index >= 0) {
                    matches.put(context, "length", 1L, false);
                    final DynArray singleMatch = new DynArray(context.getGlobalObject());
                    singleMatch.put(context, "input", string, false);
                    singleMatch.put(context, "index", (long) index, false);
                    singleMatch.put(context, "0", searchString, false);
                    singleMatch.put(context, "length", 1L, false);
                    matches.put(context, "0", singleMatch, false);
                }
            }

            Long matchCount = Types.toInteger(context, matches.get(context, "length"));
            if (matchCount > 0) {
                if (replaceValue instanceof JSFunction) {
                    for (int i = 0; i < matchCount; ++i) {
                        final DynArray nextMatch = (DynArray) matches.get(context, "" + i);
                        Object[] functionArgs = new Object[(int) (m + 3)];
                        functionArgs[0] = Types.toString(context, nextMatch.get(context, "0"));
                        if (m == 0) {
                            functionArgs[1] = nextMatch.get(context, "index");
                            functionArgs[2] = string;
                        } else {
                            int matchLength = Types.toInteger(context, nextMatch.get(context, "length")).intValue();
                            for (int j = 1; j <= matchLength; j++) {
                                functionArgs[j] = nextMatch.get(context, "" + j);
                            }
                        }
                        String replacement = Types.toString(context, context.call((JSFunction) replaceValue, Types.UNDEFINED, functionArgs));
                        string = string.replaceFirst((String) functionArgs[0], Matcher.quoteReplacement(replacement));
                    }
                } else {
                    String newString = Types.toString(context, replaceValue);
                    int startIndex = 0;
                    for (int i = 0; i < matchCount.intValue(); i++) {
                        DynArray match = (DynArray) matches.get(context, "" + i);
                        String toReplace = Types.toString(context, match.get(context, "0"));
                        String quotedReplacement = Matcher.quoteReplacement(buildReplacementString(context, newString, match));
                        if (toReplace.equals("")) {
                            return string.replaceAll(toReplace, quotedReplacement);
                        }
                        int idx = string.indexOf(toReplace, startIndex);
                        string = string.substring(0, idx) 
                                + string.substring(idx).replaceFirst(Pattern.quote(toReplace), quotedReplacement);
                        startIndex = idx+quotedReplacement.length()-1;
                    }
                }
            }
        }
        return string;
    }

    protected DynArray buildSingleMatch(ExecutionContext context, String inputString, final String searchString, long startIndex) {
        final DynArray singleMatch = new DynArray(context.getGlobalObject());
        singleMatch.put(context, "input", inputString, false);
        singleMatch.put(context, "index", (long) startIndex, false);
        singleMatch.put(context, "0", searchString, false);
        singleMatch.put(context, "length", 1L, false);
        return singleMatch;
    }

    protected String buildReplacementString(ExecutionContext context, String replaceWith, DynArray matches) {
        int fromIndex            = 0;
        int endIndex             = replaceWith.length()-1;
        StringBuilder replacement = new StringBuilder();
        
        while (fromIndex <= endIndex) {
            char nextChar;
            if ((nextChar = replaceWith.charAt(fromIndex)) == '$' && fromIndex != endIndex) {
                String input    = Types.toString(context, matches.get(context, "input"));
                String matched  = Types.toString(context, matches.get(context, "0"));
                int matchBegins = Types.toInteger(context, matches.get(context, "index")).intValue();
                switch(replaceWith.charAt(fromIndex+1)) {
                case '$':
                    replacement.append("$");
                    fromIndex++;
                    break;
                case '&':
                    replacement.append(Types.toString(context,  matches.get(context, "0")));
                    fromIndex++;
                    break;
                case '`':
                    replacement.append(input.substring(0, matchBegins));
                    fromIndex++;
                    break;
                case '\'':
                    replacement.append(input.substring(matchBegins+matched.length()));
                    fromIndex++;
                    break;
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    String backreference = Character.toString(replaceWith.charAt(fromIndex+1));
                    if (matches.get(context, backreference) != Types.UNDEFINED) {
                        replacement.append(Types.toString(context, matches.get(context, backreference)));
                        fromIndex++;
                    } else {
                        replacement.append(nextChar);
                    }
                    break;
                default:
                    replacement.append(nextChar);
                }
            } else {
                replacement.append(nextChar);
            }
            fromIndex++;
        }
        return replacement.toString();
    }

}
