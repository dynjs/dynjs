package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;

public class UnaryPlusExpression extends AbstractUnaryOperatorExpression {
    public UnaryPlusExpression(Tree tree, Expression expression) {
        super(tree, expression, "+");
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 11.4.6
                append(getExpr().getCodeBlock());
                // val
                append(jsGetValue());
                // val
                append(jsToNumber());
            }
        };
    }
}
