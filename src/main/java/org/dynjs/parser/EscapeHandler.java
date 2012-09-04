package org.dynjs.parser;

public class EscapeHandler {
    
    public EscapeHandler() {
        
    }
    
    public String handle(String esc) {
        if ( esc.startsWith ( "\\x" ) ) {
            return handleHexEscape(esc);
        }
        
        if ( esc.startsWith( "\\u" ) ) {
            return handleUnicodeEscape(esc);
        }
        
        if ( esc.equals( "\\b" ) ) {
            return "\b";
        }
        if ( esc.equals( "\\f" ) ) {
            return "\f";
        }
        if ( esc.equals( "\\n" ) ) {
            return "\n";
        }
        if ( esc.equals( "\\r" ) ) {
            return "\r";
        }
        if ( esc.equals( "\\t" ) ) {
            return "\t";
        }
        return esc.substring(1);
    }
    
    public String handleHexEscape(String esc) {
        String hexStr = esc.substring( 2 );
        int code = Integer.decode( "0x" + hexStr );
        return Character.toString( (char) code );
    }
    
    public String handleUnicodeEscape(String esc) {
        String hexStr = esc.substring(2);
        int code = Integer.decode( "0x" + hexStr );
        return new String( Character.toChars(code) );
    }

}
