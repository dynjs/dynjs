package org.dynjs.runtime.builtins.types.string.prototype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;
import org.joni.Region;

public class Replace extends AbstractNonConstructorFunction {

    public Replace(GlobalObject globalObject) {
        super(globalObject, "searchValue", "replaceValue");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Types.checkObjectCoercible(context, self);
        final String searchString = Types.toString(context, self);
        JSFunction replacement;
        if (args[1] instanceof JSFunction) {
            replacement = (JSFunction) args[1];
        } else {
            replacement = new IdentityFunction(context.getGlobalObject(), args[1]);
        }

        if (args[0] instanceof DynRegExp) {
            return replaceWithRegex(context, searchString, (DynRegExp) args[0], replacement);
        } else {
            return replaceWithString(context, searchString, Types.toString(context, args[0]), replacement);
        }
    }

    private Object replaceWithString(ExecutionContext context, String searchString, String query, JSFunction function) {
        int index = searchString.indexOf(query);
        if (index < 0) {
            return searchString;
        }

        Match match = Match.fromStartAndLength(searchString, index, query.length());

        String replacement = match.buildReplacementString(
                Types.toString(context, context.call(function, Types.UNDEFINED, match.toFnArgs())));

        return searchString.replaceFirst(Pattern.quote(query), Matcher.quoteReplacement(replacement));
    }

    private Object replaceWithRegex(ExecutionContext context, String searchString, DynRegExp regexp, JSFunction function) {
        final StringBuilder result = new StringBuilder();

        Region region;
        Match lastMatch = null;
        int startIndex = 0;
        while ((region = regexp.match(context, searchString, startIndex)) != null) {
            Match match = Match.fromRegion(searchString, region);
            lastMatch = match;

            String replacement = match.buildReplacementString(
                    Types.toString(context, context.call(function, Types.UNDEFINED, match.toFnArgs())));
            result.append(searchString.substring(startIndex, match.start()))
                    .append(replacement);

            if (startIndex == match.end()) {
                result.append(searchString.substring(startIndex, Math.min(startIndex + 1, searchString.length())));
                startIndex++;
            } else {
                startIndex = match.end();
            }

            if (!regexp.isGlobal()) {
                break;
            }
        }
        if (lastMatch == null) {
            result.append(searchString);
        } else {
            result.append(lastMatch.rest());
        }

        return result.toString();
    }

    private static class IdentityFunction extends AbstractNativeFunction {
        private final Object value;

        public IdentityFunction(GlobalObject globalObject, Object value) {
            super(globalObject);
            this.value = value;
        }

        @Override
        public Object call(ExecutionContext context, Object self, Object... args) {
            return value;
        }
    }

    private static class Match {
        private String searchString;
        private List<Capture> captures;

        public Match(String searchString, List<Capture> captures) {
            this.searchString = searchString;
            this.captures = captures;
        }

        public static Match fromStartAndLength(String searchString, int index, int length) {
            return new Match(searchString, Arrays.asList(new Capture(searchString, index, index + length)));
        }

        public static Match fromRegion(String searchString, Region region){
            List<Capture> captures = new ArrayList<>();
            for(int i = 0; i < region.numRegs; i++){
                captures.add(new Capture(searchString, region.beg[i], region.end[i]));
            }

            return new Match(searchString, captures);
        }

        public Object[] toFnArgs() {
            Object[] fnArgs = new Object[3 + captures.size()];
            fnArgs[captures.size()] = start();
            fnArgs[1 + captures.size()] = searchString();
            for (int i = 0; i < captures.size(); i++) {
                fnArgs[i] = captures.get(i).capture();
            }
            return fnArgs;
        }

        protected String buildReplacementString(String replaceWith) {
            int fromIndex = 0;
            int endIndex = replaceWith.length() - 1;
            StringBuilder replacement = new StringBuilder();

            while (fromIndex <= endIndex) {
                char nextChar;
                if ((nextChar = replaceWith.charAt(fromIndex)) == '$' && fromIndex != endIndex) {
                    switch (replaceWith.charAt(fromIndex + 1)) {
                        case '$':
                            replacement.append("$");
                            fromIndex++;
                            break;
                        case '&':
                            replacement.append(captures.get(0).capture());
                            fromIndex++;
                            break;
                        case '`':
                            replacement.append(searchString.substring(0, captures.get(0).start));
                            fromIndex++;
                            break;
                        case '\'':
                            replacement.append(searchString.substring(captures.get(0).end));
                            fromIndex++;
                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            Integer backreference = Integer.parseInt(String.valueOf(replaceWith.charAt(fromIndex + 1)));
                            if (backreference < captures.size()) {
                                replacement.append(captures.get(backreference).capture());
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

        public int start() {
            return captures.get(0).start;
        }

        public String searchString() {
            return searchString;
        }

        public int end() {
            return captures.get(0).end;
        }

        public String rest() {
            return searchString.substring(Math.min(searchString.length(), end()));
        }
    }

    private static class Capture {
        private final String original;
        private final int start;
        private final int end;

        public Capture(String original, int start, int end) {
            this.original = original;
            this.start = start;
            this.end = end;
        }

        public Object capture() {
            if (start < 0 || end < 0) { return Types.UNDEFINED; }
            return original.substring(start, Math.min(original.length(), end));
        }
    }
}
