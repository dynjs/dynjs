package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class BitwiseExpression extends AbstractBinaryExpression {


    public BitwiseExpression(Expression lhs, Expression rhs, String op) {
        super(lhs, rhs, op);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Object lhs = getValue(this.lhsGet, context, getLhs().interpret(context, debug));

        Long lhsNum = null;

        if (getOp().equals(">>>")) {
            lhsNum = Types.toUint32(context, lhs);
        } else {
            lhsNum = Types.toInt32(context, lhs);
        }

        Object value = getRhs().interpret(context, debug);

        if (getOp().equals("<<")) {
            // 11.7.1
            Long rhsNum = Types.toUint32(context, getValue(this.rhsGet, context, value));
            int shiftCount = rhsNum.intValue() & 0x1F;
            return((int) (lhsNum.longValue() << shiftCount));
        } else if (getOp().equals(">>")) {
            // 11.7.2
            Long rhsNum = Types.toUint32(context, getValue(this.rhsGet, context, value));
            int shiftCount = rhsNum.intValue() & 0x1F;
            return((int) (lhsNum.longValue() >> shiftCount));
        } else if (getOp().equals(">>>")) {
            // 11.7.3
            Long rhsNum = Types.toUint32(context, getValue(this.rhsGet, context, value));
            int shiftCount = rhsNum.intValue() & 0x1F;
            return(lhsNum.longValue() >>> shiftCount);
        } else if (getOp().equals("&")) {
            Long rhsNum = Types.toInt32(context, getValue(this.rhsGet, context, value));
            return(lhsNum.longValue() & rhsNum.longValue());
        } else if (getOp().equals("|")) {
            Long rhsNum = Types.toInt32(context, getValue(this.rhsGet, context, value));
            return(lhsNum.longValue() | rhsNum.longValue());
        } else if (getOp().equals("^")) {
            Long rhsNum = Types.toInt32(context, getValue(this.rhsGet, context, value));
            return(lhsNum.longValue() ^ rhsNum.longValue());
        }

        return null; // not reached
    }

}
