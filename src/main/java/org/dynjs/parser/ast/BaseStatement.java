package org.dynjs.parser.ast;

import org.dynjs.parser.js.Position;

public abstract class BaseStatement extends AbstractStatement {

    private Position position;
    
    public BaseStatement(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }


}
