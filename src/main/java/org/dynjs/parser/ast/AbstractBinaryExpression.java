package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.runtime.ExecutionContext;

public abstract class AbstractBinaryExpression extends AbstractExpression {

    private Expression lhs;
    private Expression rhs;
    private String op;

    AbstractBinaryExpression(final Tree tree, final Expression lhs, final Expression rhs, String op) {
        super(tree);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }
    
    public void verify(ExecutionContext context, boolean strict) {
        this.lhs.verify( context, strict);
        this.rhs.verify( context, strict);
    }

    public Expression getLhs() {
        return this.lhs;
    }

    public Expression getRhs() {
        return this.rhs;
    }

    public String getOp() {
        return this.op;
    }

    public String toString() {
        return this.lhs + " " + this.op + " " + this.rhs;
    }

}
