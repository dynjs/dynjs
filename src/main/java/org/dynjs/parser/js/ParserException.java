package org.dynjs.parser.js;

import org.dynjs.exception.DynJSException;

public class ParserException extends DynJSException {
    
    private Position position;

    public ParserException(String message) {
        super( message );
    }
    
    public ParserException(Throwable cause) {
        super( cause );
    }
    
    public ParserException(Position position, String message) {
        super( message );
        this.position = position;
    }
    
    @Override
    public String getMessage() {
        if ( this.position != null ) {
            return position.getFileName() + ":" + position.getLine() + ":" + position.getColumn() + ": " + super.getMessage();
        }
        return super.getMessage();
    }

}
