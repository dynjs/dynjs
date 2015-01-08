package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public abstract class PropertyAssignment {

    private Position position;
    private String name;

    public PropertyAssignment(Position position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Position getPosition() {
        return this.position;
    }

    public abstract Object accept(Object context, CodeVisitor visitor, boolean strict);

    public abstract int getSizeMetric();

    public abstract Object interpret(ExecutionContext context, boolean debug);

}
