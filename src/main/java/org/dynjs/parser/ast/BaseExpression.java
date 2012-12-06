package org.dynjs.parser.ast;

import org.dynjs.parser.js.Position;

public abstract class BaseExpression extends AbstractExpression {

    private Position position;

    public BaseExpression(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

}
