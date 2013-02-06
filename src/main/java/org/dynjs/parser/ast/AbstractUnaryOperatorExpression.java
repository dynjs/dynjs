package org.dynjs.parser.ast;

import java.util.ArrayList;
import java.util.List;

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
    
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.expr.getFunctionDeclarations();
    }
    
    public int getSizeMetric() {
        return this.expr.getSizeMetric() + 3;
    }

}
