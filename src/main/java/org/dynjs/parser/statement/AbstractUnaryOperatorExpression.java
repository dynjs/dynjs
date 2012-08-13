package org.dynjs.parser.statement;

import org.antlr.runtime.tree.Tree;

public abstract class AbstractUnaryOperatorExpression extends AbstractExpression {
    
    private Expression expr;
    private String op;

    public AbstractUnaryOperatorExpression(Tree tree, Expression expr, String op) {
        super( tree );
        this.expr = expr;
        this.op = op;
    }
    
    public String getOp() {
        return this.op;
    }
    
    public Expression getExpr() {
        return this.expr;
    }

}
