package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class FloatingNumberExpression extends NumberLiteralExpression {
    double value;

    public FloatingNumberExpression(Position position, String text, int radix, double value) {
        super(position, text, radix);

        this.value = value;
    }

    @Override
    public void accept(Object context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

    public double getValue() {
        return value;
    }
}
