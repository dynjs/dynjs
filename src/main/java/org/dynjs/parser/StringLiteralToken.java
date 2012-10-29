package org.dynjs.parser;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

public class StringLiteralToken extends CommonToken {

    private static final long serialVersionUID = -3384930971992408807L;
    
    private boolean continued;
    private boolean escaped;
    
    public StringLiteralToken(Token original, String text, boolean continued, boolean escaped) {
        super( original );
        setType(JavascriptParser.StringLiteral);
        setText(text);
        this.continued = continued;
        this.escaped = escaped;
    }
    
    public boolean isEscaped() {
        return this.escaped;
    }
    
    public boolean isContinued() {
        return this.continued;
    }

}
