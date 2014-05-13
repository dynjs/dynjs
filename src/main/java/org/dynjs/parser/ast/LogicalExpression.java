package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class LogicalExpression extends AbstractBinaryExpression {

    private final CallSite lhsGet = DynJSBootstrapper.factory().createGet();

    public LogicalExpression(Expression lhs, Expression rhs, String op) {
        super(lhs, rhs, op);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context) {
        Object lhs = getValue(this.lhsGet, context, getLhs().interpret(context));

        if ((getOp().equals("||") && Types.toBoolean(lhs)) || (getOp().equals("&&") && !Types.toBoolean(lhs))) {
            return(lhs);
        } else {
            return getRhs().interpret(context);
        }
    }

}
