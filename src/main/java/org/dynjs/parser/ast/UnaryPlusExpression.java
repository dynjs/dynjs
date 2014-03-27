package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class UnaryPlusExpression extends AbstractUnaryOperatorExpression {
    public UnaryPlusExpression(Expression expression) {
        super(expression, "+");
    }

    public String toString() {
        return "+" + getExpr();
    }

    @Override
    public void accept(Object context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }
}
