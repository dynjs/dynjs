package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class UnaryMinusExpression extends AbstractUnaryOperatorExpression {

    public UnaryMinusExpression(Tree tree, Expression expr) {
        super(tree, expr, "-");
    }

    public String toString() {
        return "-" + getExpr();
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);

    }
}
