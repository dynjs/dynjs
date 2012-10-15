package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.runtime.ExecutionContext;

public abstract class AbstractUnaryOperatorExpression extends AbstractExpression {

    private Expression expr;
    private String op;

    public AbstractUnaryOperatorExpression(Tree tree, Expression expr, String op) {
        super(tree);
        this.expr = expr;
        this.op = op;
    }

    public String getOp() {
        return this.op;
    }

    public Expression getExpr() {
        return this.expr;
    }
    
    public void verify(ExecutionContext context, boolean strict) {
        this.expr.verify(context, strict);
    }

}
