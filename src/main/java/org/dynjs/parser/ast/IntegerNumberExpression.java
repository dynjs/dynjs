package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class IntegerNumberExpression extends NumberLiteralExpression {
    private long value;

    public IntegerNumberExpression(Position position, String text, int radix, long value) {
        super(position, text, radix);

        this.value = value;
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

    public long getValue() {
        return value;
    }
}
