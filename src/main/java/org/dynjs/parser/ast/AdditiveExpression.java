package org.dynjs.parser.ast;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class AdditiveExpression extends AbstractBinaryExpression {

    public AdditiveExpression(final Expression lhs, final Expression rhs, final String op) {
        super(lhs, rhs, op);
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict);
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {

        if ( this.getOp().equals( "+" ) ) {
            Object lhs = Types.toPrimitive(context, getValue(this.lhsGet, context, getLhs().interpret(context, debug)));
            Object rhs = Types.toPrimitive(context, getValue(this.rhsGet, context, getRhs().interpret(context, debug)));

            if (lhs instanceof String || rhs instanceof String) {
                return(Types.toString(context, lhs) + Types.toString(context, rhs));

            }

            Number lhsNum = Types.toNumber(context, lhs);
            Number rhsNum = Types.toNumber(context, rhs);

            if (Double.isNaN(lhsNum.doubleValue()) || Double.isNaN(rhsNum.doubleValue())) {
                return(Double.NaN);

            }

            if (lhsNum instanceof Double || rhsNum instanceof Double) {
                if (lhsNum.doubleValue() == 0.0 && rhsNum.doubleValue() == 0.0) {
                    if (Double.compare(lhsNum.doubleValue(), 0.0) < 0 && Double.compare(rhsNum.doubleValue(), 0.0) < 0) {
                        return(-0.0);

                    } else {
                        return(0.0);

                    }
                }
                return(lhsNum.doubleValue() + rhsNum.doubleValue());

            }

            return(lhsNum.longValue() + rhsNum.longValue());

        } else {
            Number lhs = Types.toNumber(context, getValue(this.lhsGet, context, getLhs().interpret(context, debug)));
            Number rhs = Types.toNumber(context, getValue(this.rhsGet, context, getRhs().interpret(context, debug)));

            if (Double.isNaN(lhs.doubleValue()) || Double.isNaN(rhs.doubleValue())) {
                return(Double.NaN);

            }

            if (lhs instanceof Double || rhs instanceof Double) {
                if (lhs.doubleValue() == 0.0 && rhs.doubleValue() == 0.0) {
                    if (Double.compare(lhs.doubleValue(), 0.0) < 0 && Double.compare(rhs.doubleValue(), 0.0) < 0) {
                        return(+0.0);

                    }

                }
                return(lhs.doubleValue() - rhs.doubleValue());

            }

            return(lhs.longValue() - rhs.longValue());
        }
    }

}
