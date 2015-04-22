package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.BuiltinNumber;

public class MultiplicativeExpression extends AbstractBinaryExpression {

    public MultiplicativeExpression(Expression lhs, Expression rhs, String op) {
        super(lhs, rhs, op);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        Number lval = Types.toNumber(context, getValue(this.lhsGet, context, getLhs().interpret( context, debug) ) );
        Number rval = Types.toNumber(context, getValue(this.rhsGet, context, getRhs().interpret(context, debug)) );

        if (Double.isNaN(lval.doubleValue()) || Double.isNaN(rval.doubleValue())) {
            return(Double.NaN);

        }

        if (lval instanceof Double || rval instanceof Double) {
            switch (getOp()) {
                case "*":
                    return(lval.doubleValue() * rval.doubleValue());

                case "/":
                    // Divide-by-zero
                    if (NumericHelper.isZero(rval)) {
                        if (NumericHelper.isZero(lval)) {
                            return(Double.NaN);

                        } else if (NumericHelper.isSameSign(lval, rval)) {
                            return(Double.POSITIVE_INFINITY);

                        } else {
                            return(Double.NEGATIVE_INFINITY);
                        }

                        // Zero-divided-by-something
                    } else if (NumericHelper.isZero(lval)) {
                        if (NumericHelper.isSameSign(lval, rval)) {
                            return(0L);
                        } else {
                            return(-0.0);
                        }
                    }

                    // Regular math
                    double primaryValue = lval.doubleValue() / rval.doubleValue();
                    if (NumericHelper.isRepresentableByLong(primaryValue)) {
                        return((long) primaryValue);
                    } else {
                        return(primaryValue);
                    }

                case "%":
                    if (rval.doubleValue() == 0.0) {
                        return(Double.NaN);

                    }
                    return(BuiltinNumber.modulo(lval, rval));

            }
        } else {
            switch (getOp()) {
                case "*":
                    return(lval.longValue() * rval.longValue());

                case "/":
                    if (rval.longValue() == 0L) {
                        if (lval.longValue() == 0L) {
                            return(Double.NaN);

                        } else if (NumericHelper.isSameSign(lval, rval)) {
                            return(Double.POSITIVE_INFINITY);

                        } else {
                            return(Double.NEGATIVE_INFINITY);
                        }
                    }

                    if (lval.longValue() == 0) {
                        if (Double.compare(rval.doubleValue(), 0.0) > 0) {
                            return(0L);

                        } else {
                            return(-0.0);

                        }
                    }
                    double primaryResult = lval.doubleValue() / rval.longValue();
                    if (primaryResult == (long) primaryResult) {
                        return((long) primaryResult);
                    } else {
                        return(primaryResult);
                    }

                case "%":
                    if (rval.longValue() == 0L) {
                        return(Double.NaN);

                    }

                    return(BuiltinNumber.modulo(lval, rval));
            }
        }

        return null; // not reached

    }
}
