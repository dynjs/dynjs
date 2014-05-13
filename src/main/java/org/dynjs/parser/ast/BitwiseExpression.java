package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class BitwiseExpression extends AbstractBinaryExpression {

    public BitwiseExpression(Expression lhs, Expression rhs, String op) {
        super(lhs, rhs, op);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

}
