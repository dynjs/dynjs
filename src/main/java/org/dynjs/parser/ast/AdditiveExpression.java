package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class AdditiveExpression extends AbstractBinaryExpression {

    public AdditiveExpression(final Tree tree, final Expression lhs, final Expression rhs, final String op) {
        super(tree, lhs, rhs, op);
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict);
    }

}
