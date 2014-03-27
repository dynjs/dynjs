package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class UnaryMinusExpression extends AbstractUnaryOperatorExpression {

    public UnaryMinusExpression(Expression expr) {
        super(expr, "-");
    }

    public String toString() {
        return "-" + getExpr();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }
}
