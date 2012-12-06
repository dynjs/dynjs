package org.dynjs.parser.ast;

import org.dynjs.parser.js.Position;

public abstract class AbstractUnaryOperatorExpression extends AbstractExpression {

    private Expression expr;
    private String op;

    public AbstractUnaryOperatorExpression(Expression expr, String op) {
        this.expr = expr;
        this.op = op;
    }
    
    public Position getPosition() {
        return this.expr.getPosition();
    }

    public String getOp() {
        return this.op;
    }

    public Expression getExpr() {
        return this.expr;
    }

}
