package org.dynjs.parser.js;

public enum TokenType {

    BREAK("break"),
    DO("do"),
    INSTANCEOF("instanceof"),
    TYPEOF("typeof"),
    CASE("case"),
    ELSE("else"),
    NEW("new"),
    VAR("var"),
    CATCH("catch"),
    FINALLY("finally"),
    RETURN("return"),
    VOID("void"),
    CONTINUE("continue"),
    FOR("for"),
    SWITCH("switch"),
    WHILE("while"),
    DEBUGGER("debugger"),
    FUNCTION("function"),
    THIS("this"),
    WITH("with"),
    DEFAULT("default"),
    IF("if"),
    THROW("throw"),
    DELETE("delete"),
    IN("in"),
    OF("of"),
    TRY("try"),
    
    PRINT("print"),

    EOF("end-of-file", true, false ),
    IDENTIFIER("identifier"),

    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),

    DOT("."),
    SEMICOLON(";"),
    COMMA(","),

    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_EQUAL("<="),
    GREATER_THAN_EQUAL(">="),

    EQUALS("="),
    PLUS_EQUALS("+="),
    MINUS_EQUALS("-="),
    MULTIPLY_EQUALS("*="),
    MODULO_EQUALS("%="),
    DIVIDE_EQUALS("/="),

    LEFT_SHIFT_EQUALS("<<="),
    RIGHT_SHIFT_EQUALS(">>="),
    UNSIGNED_RIGHT_SHIFT_EQUALS(">>>="),

    BITWISE_AND_EQUALS("&="),
    BITWISE_OR_EQUALS("|="),
    BITWISE_XOR_EQUALS("^="),

    EQUALITY("=="),
    NOT_EQUALITY("!="),

    STRICT_EQUALITY("==="),
    STRICT_NOT_EQUALITY("!=="),

    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    MODULO("%"),
    DIVIDE("/"),

    PLUS_PLUS("++"),
    MINUS_MINUS("--"),

    LEFT_SHIFT ("<<"),
    RIGHT_SHIFT(">>"),
    UNSIGNED_RIGHT_SHIFT(">>>"),

    BITWISE_AND("&"),
    BITWISE_OR("|"),
    BITWISE_XOR("^"),

    NOT("!"),

    INVERSION("~"),

    LOGICAL_AND("&&"),
    LOGICAL_OR("||"),

    QUESTION("?"),
    COLON(":"),

    /*
    TAB("\\t", true),
    VERTICAL_TAB("\\v", true),
    FORM_FEED("\\f", true),
    SPACE(" ", true),
    NOBREAK_SPACE("no-break space", true),
    BYTE_ORDER_MARK("byte-order mark", true),
    USP("Unicode space", true),
    */

    LINE_SEPARATOR("line-separator", true, true),
    PARAGRAPH_SEPARATOR("paragraph-separator", true, true),
    
    CR( "\\r", true, true ),
    NL( "\\n", true, true ),
    CRNL( "\\r\\n", true, true ),

    //SINGLE_LINE_COMMENT("single-line comment", true),
    //MULTI_LINE_COMMENT("multi-line comment", true),

    NULL("null"),
    TRUE("true"),
    FALSE("false"),

    DECIMAL_LITERAL("decimal literal"),
    HEX_LITERAL("hex literal"),
    OCTAL_LITERAL("octal literal"),
    STRING_LITERAL("string literal"),
    REGEXP_LITERAL("regexp literal" ),

    ;

    private String description;
    private final boolean unprintable;
    private boolean skipable;

    TokenType(String description, boolean unprintable, boolean skipable) {
        this.description = description;
        this.unprintable = unprintable;
        this.skipable = skipable;
    }

    TokenType(String description ) {
        this(description, false, false);
    }

    public boolean isSkippable() {
        return this.skipable;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isUnprintable() {
        return this.unprintable;
    }

    public String toString() {
        return this.description;
    }

}
