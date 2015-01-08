package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class NamedValue extends PropertyAssignment {

    private Expression expr;

    public NamedValue(Position position, String name, Expression expr) {
        super(position, name);
        this.expr = expr;
    }

    public Expression getExpr() {
        return this.expr;
    }

    public String toString() {
        return getName() + ":" + this.expr;
    }
    
    public int getSizeMetric() {
        return this.expr.getSizeMetric() + 3;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

    public Object interpret(ExecutionContext context, boolean debug) {
        return getExpr().interpret(context, debug);
    }
}
