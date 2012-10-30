package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class MultiplicativeExpression extends AbstractBinaryExpression {

    public MultiplicativeExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super(tree, lhs, rhs, op);
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
