package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class UnaryPlusExpression extends AbstractUnaryOperatorExpression {
    public UnaryPlusExpression(Tree tree, Expression expression) {
        super(tree, expression, "+");
    }

    public String toString() {
        return "+" + getExpr();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }
}
