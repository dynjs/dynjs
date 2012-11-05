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
                        matches = (DynArray) object;
                    }
                    m = Types.toInteger(context, matches.get(context, "length")).intValue()-1;
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
                matches.put(context, "input", string, false);
                matches.put(context, "index", (long) index, false);
                matches.put(context, "0", searchString, false);
                matches.put(context, "length", 1L, false);
            }

            Long matchCount = Types.toInteger(context, matches.get(context, "length"));
            if (matchCount > 0) {
                if (replaceValue instanceof JSFunction) {
                    for (int i = 0; i < matchCount; ++i) {
                        String nextMatch = Types.toString(context, matches.get(context, "" + i));
                        Object[] functionArgs = new Object[(int) (m + 3)];
                        functionArgs[0] = nextMatch;
                        if (m == 0) {
                            functionArgs[1] = matches.get(context, "index");
                            functionArgs[2] = string;
                        } else {
                            for (int j = 1; j < m; j++) {
                                functionArgs[j] = matches.get(context, "" + j);
                            }
                        }
                        String replacement = Types.toString(context, context.call((JSFunction) replaceValue, Types.UNDEFINED, functionArgs));
                        string = string.replaceFirst(nextMatch, Matcher.quoteReplacement(replacement));
                    }
                } else {
                    String newString = Types.toString(context, replaceValue);
                    if (global != Boolean.TRUE) {
                        string = string.replaceFirst((String) matches.get(context, "0"), Matcher.quoteReplacement(buildReplacementString(context, newString, matches)));
                    } else {
                        for (int i=0; i<matchCount.intValue(); i++) {
                            DynArray match = (DynArray) matches.get(context, ""+i);
                            string = string.replaceFirst((String) match.get(context, "0"), Matcher.quoteReplacement(buildReplacementString(context, newString, match)));
                        }
                    }
                }
            }
        }
        return string;
    }

    protected String buildReplacementString(ExecutionContext context, String replaceWith, DynArray matches) {
        int fromIndex            = 0;
        int endIndex             = replaceWith.length()-1;
        StringBuffer replacement = new StringBuffer();
        
        while (fromIndex <= endIndex) {
            char nextChar;
            if ((nextChar = replaceWith.charAt(fromIndex)) == '$' && fromIndex != endIndex) {
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
                    String input = Types.toString(context, matches.get(context, "input"));
                    int end      = Types.toInteger(context, matches.get(context, "index")).intValue();
                    replacement.append(input.substring(0, end));
                    fromIndex++;
                    break;
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    final String backreference = Character.toString(replaceWith.charAt(fromIndex+1));
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
