package org.dynjs.parser.js;

public class SyntaxError extends ParserException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public SyntaxError(String message) {
        super( message );
    }
    
    public SyntaxError(Position position, String message) {
        super( position, message );
    }

}
