package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

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

    public String toString() {
        return "+" + getExpr();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }
}
