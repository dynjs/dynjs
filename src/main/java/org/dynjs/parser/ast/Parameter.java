package org.dynjs.parser.ast;

import org.dynjs.parser.js.Position;

public class Parameter {
    
    private Position position;
    private String identifier;

    public Parameter(Position position, String identifier) {
        this.position = position;
        this.identifier = identifier;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }

}
