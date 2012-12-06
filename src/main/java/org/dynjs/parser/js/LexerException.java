package org.dynjs.parser.js;

import org.dynjs.exception.DynJSException;

public class LexerException extends DynJSException {
    
    private static final long serialVersionUID = -7515874777094452705L;

    public LexerException(String message) {
        super( message );
    }

    public LexerException(Throwable cause) {
        super( cause );
    }
}
