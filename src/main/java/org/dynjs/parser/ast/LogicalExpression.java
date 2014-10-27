package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

public class LogicalExpression extends AbstractBinaryExpression {

    public LogicalExpression(Expression lhs, Expression rhs, String op) {
        super(lhs, rhs, op);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object lhs = getValue(this.lhsGet, context, getLhs().interpret(context, debug));

        if ((getOp().equals("||") && Types.toBoolean(lhs)) || (getOp().equals("&&") && !Types.toBoolean(lhs))) {
            return(lhs);
        } else {
            return getRhs().interpret(context, debug);
        }
    }

}
