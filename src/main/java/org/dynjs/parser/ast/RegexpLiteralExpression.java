package org.dynjs.parser.ast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.parser.js.SyntaxError;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;

public class RegexpLiteralExpression extends BaseExpression implements IllegalFunctionMemberExpression {

    static class RegexpLiteralExpressionParser {
        // \u0085 is not a line terminator in JS but is in Java regexes
        private static final String REG_EXP_PATTERN = "^\\/((?:.|\u0085)*)\\/([igm]{0,})$";

        static RegexpLiteralExpressionParser parse(String text) {
            Pattern pattern = Pattern.compile(REG_EXP_PATTERN);
            Matcher matcher = pattern.matcher(text);
                    
            if (matcher.matches()) {
                return new RegexpLiteralExpressionParser(matcher.group(1), matcher.group(2));
            }

            return null;
        }

        private final String source;
        private final String flags;

        private RegexpLiteralExpressionParser(String source, String flags) {
            this.source = source;
            this.flags = flags;
        }

        public String getPattern() {
            return source;
        }

        public String getFlags() {
            return flags;
        }
    }

    private String pattern;
    private String flags;

    public RegexpLiteralExpression(Position position, String text) {
        super(position);
        
        RegexpLiteralExpressionParser parser = RegexpLiteralExpressionParser.parse(text);
        if (parser == null) {
            throw new SyntaxError(position, "Invalid regular expression");
        }
        this.pattern = parser.getPattern();
        this.flags = parser.getFlags();
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getFlags() {
        return this.flags;
    }

    public String toString() {
        return "/" + this.pattern + "/" + this.flags;
    }
    
    public int getSizeMetric() {
        return 3;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public Object interpret(ExecutionContext context, boolean debug) {
        return(BuiltinRegExp.newRegExp((ExecutionContext) context, getPattern(), getFlags()));
    }

}
