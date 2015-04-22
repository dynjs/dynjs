package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

import java.lang.invoke.CallSite;

public class UnaryPlusExpression extends AbstractUnaryOperatorExpression {

    private final CallSite get;

    public UnaryPlusExpression(Expression expression) {
        super(expression, "+");
        this.get = DynJSBootstrapper.factory().createGet( expression.getPosition() );
    }

    public String toString() {
        return "+" + getExpr();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        return Types.toNumber(context, getValue(this.get, context, getExpr().interpret(context, debug)) );
    }
}
