package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.ParserException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

public class RegexpLiteralExpression extends AbstractExpression {

    static class RegexpLiteralExpressionParser {
        private static final String REG_EXP_PATTERN = "^\\/(.*)\\/([igm]{0,})$";

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

    public RegexpLiteralExpression(Tree tree, String text) {
        super(tree);

        RegexpLiteralExpressionParser parser = RegexpLiteralExpressionParser
                .parse(text);
        if (parser == null) {
            throw new ParserException("Invalid regular expression", tree);
        }
        this.pattern = parser.getPattern();
        this.flags = parser.getFlags();
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // context
                ldc( pattern );
                // context pattern
                ldc( flags );
                // context pattern flags
                invokestatic(p(BuiltinRegExp.class), "newRegExp", sig(DynRegExp.class, ExecutionContext.class, String.class, String.class));
                // regexp
            }
        };
    }

    public String toString() {
        return "/" + this.pattern + "/" + this.flags;
    }
}
