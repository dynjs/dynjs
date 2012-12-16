package org.dynjs.parser.js;

import static org.dynjs.parser.js.TokenType.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Lexer {

    @SuppressWarnings("serial")
    private static Set<String> KEYWORDS = new HashSet<String>() {
        {
            add("break");
            add("do");
            add("instanceof");
            add("typeof");
            add("case");
            add("else");
            add("new");
            add("var");
            add("catch");
            add("finally");
            add("return");
            add("void");
            add("continue");
            add("for");
            add("switch");
            add("while");
            add("debugger");
            add("function");
            add("this");
            add("with");
            add("default");
            add("if");
            add("throw");
            add("delete");
            add("in");
            add("try");

            add("null");
            add("true");
            add("false");

            add("print");
        }
    };

    private CharStream stream;
    private String fileName = "<eval>";
    private int lineNumber;
    private int columnNumber;

    private TokenType lastTokenType;

    public Lexer(CharStream stream) {
        this.stream = stream;
        this.lineNumber = 1;
        this.columnNumber = 0;
    }

    public void setFileName(String fileName) {
        if (fileName == null) {
            this.fileName = "<eval>";
        } else {
            this.fileName = fileName;
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    protected char la() {
        try {
            return this.stream.peek();
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }

    protected char la(int pos) {
        try {
            return this.stream.peek(pos);
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }

    protected char consume() {
        try {
            ++this.columnNumber;
            return this.stream.consume();
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }

    protected Token newToken(TokenType type, String text) {
        if (!type.isSkippable()) {
            this.lastTokenType = type;
        }
        Token token = null;

        if (text == null) {
            token = new Token(type, text, this.fileName, this.lineNumber, this.columnNumber);
        } else {
            token = new Token(type, text, this.fileName, this.lineNumber, this.columnNumber - text.length());
        }

        //System.err.println(token);

        return token;
    }

    protected void incrementLine() {
        ++this.lineNumber;
        this.columnNumber = 0;
    }

    private boolean isRegexpEnabled() {
        if (this.lastTokenType == null) {
            return true;
        }
        switch (this.lastTokenType) {
        case IDENTIFIER:
        case NULL:
        case TRUE:
        case FALSE:
        case THIS:
        case DECIMAL_LITERAL:
        case HEX_LITERAL:
        case STRING_LITERAL:
        case RIGHT_BRACKET:
        case RIGHT_PAREN:
            return false;
        default:
            return true;
        }
    }

    public Token nextToken() throws LexerException {
        char d;

        Token token = null;

        loop: while (token == null) {
            char c = la();
            switch (c) {
            case 0:
                token = newToken(EOF, null);
                break loop;
            case '{':
                consume();
                token = newToken(LEFT_BRACE, "{");
                break loop;
            case '}':
                consume();
                token = newToken(RIGHT_BRACE, "}");
                break loop;
            case '(':
                consume();
                token = newToken(LEFT_PAREN, "(");
                break loop;
            case ')':
                consume();
                token = newToken(RIGHT_PAREN, ")");
                break loop;
            case '[':
                consume();
                token = newToken(LEFT_BRACKET, "[");
                break loop;
            case ']':
                consume();
                token = newToken(RIGHT_BRACKET, "[");
                break loop;
            case '.':
                d = la(2);
                if (d >= '0' && d <= '9') {
                    token = decimalLiteral();
                    break loop;
                }
                consume();
                token = newToken(DOT, ".");
                break loop;
            case ';':
                consume();
                token = newToken(SEMICOLON, ";");
                break loop;
            case ',':
                consume();
                token = newToken(COMMA, ",");
                break loop;
            case ':':
                consume();
                token = newToken(COLON, ":");
                break loop;
            case '?':
                consume();
                token = newToken(QUESTION, "?");
                break loop;
            case '<':
                consume();
                d = la();
                switch (d) {
                case '=':
                    consume();
                    token = newToken(LESS_THAN_EQUAL, "<=");
                    break loop;
                case '<':
                    consume();
                    if (la() == '=') {
                        consume();
                        token = newToken(LEFT_SHIFT_EQUALS, "<<=");
                        break loop;
                    }
                    token = newToken(LEFT_SHIFT, "<<");
                    break loop;
                }
                token = newToken(LESS_THAN, ">");
                break loop;
            case '>':
                consume();
                d = la();

                switch (d) {
                case '=':
                    consume();
                    token = newToken(GREATER_THAN_EQUAL, ">=");
                    break loop;
                case '>':
                    consume();
                    d = la();
                    switch (d) {
                    case '>':
                        consume();
                        if (la() == '=') {
                            consume();
                            token = newToken(UNSIGNED_RIGHT_SHIFT_EQUALS, ">>>=");
                            break loop;
                        }
                        token = newToken(UNSIGNED_RIGHT_SHIFT, ">>>");
                        break loop;
                    case '=':
                        consume();
                        token = newToken(RIGHT_SHIFT_EQUALS, ">>>");
                        break loop;
                    }
                    token = newToken(RIGHT_SHIFT, ">>");
                    break loop;
                }
                token = newToken(GREATER_THAN, ">");
                break loop;
            case '=':
                consume();
                if (la() == '=') {
                    consume();
                    if (la() == '=') {
                        consume();
                        token = newToken(STRICT_EQUALITY, "===");
                        break loop;
                    }
                    token = newToken(EQUALITY, "==");
                    break loop;
                }
                token = newToken(EQUALS, "=");
                break loop;
            case '!':
                consume();
                if (la() == '=') {
                    consume();
                    if (la() == '=') {
                        consume();
                        token = newToken(STRICT_NOT_EQUALITY, "!==");
                        break loop;
                    }
                    token = newToken(NOT_EQUALITY, "!=");
                    break loop;
                }
                token = newToken(NOT, "!");
                break loop;
            case '+':
                d = la(2);
                switch (d) {
                case '+':
                    consume();
                    consume();
                    token = newToken(PLUS_PLUS, "++");
                    break loop;
                case '=':
                    consume();
                    consume();
                    token = newToken(PLUS_EQUALS, "+=");
                    break loop;
                }
                consume();
                token = newToken(PLUS, "+");
                break loop;
            case '-':
                d = la(2);
                switch (d) {
                case '-':
                    consume();
                    consume();
                    token = newToken(MINUS_MINUS, "--");
                    break loop;
                case '=':
                    consume();
                    consume();
                    token = newToken(MINUS_EQUALS, "-=");
                    break loop;
                }
                consume();
                token = newToken(MINUS, "-");
                break loop;
            case '*':
                consume();
                if (la() == '=') {
                    consume();
                    token = newToken(MULTIPLY_EQUALS, "*=");
                    break loop;
                }
                token = newToken(MULTIPLY, "*");
                break loop;
            case '/':
                consume();
                d = la();
                switch (d) {
                case '=':
                    consume();
                    token = newToken(DIVIDE_EQUALS, "/=");
                    break loop;
                case '/':
                    singleLineComment();
                    continue loop;
                case '*':
                    multiLineComment();
                    continue loop;
                }
                if (isRegexpEnabled()) {
                    token = regexpLiteral();
                    break loop;
                }
                token = newToken(DIVIDE, "/");
                break loop;
            case '%':
                consume();
                if (la() == '=') {
                    consume();
                    token = newToken(MODULO_EQUALS, "%=");
                    break loop;
                }
                token = newToken(MODULO, "%");
                break loop;
            case '|':
                consume();
                d = la();
                switch (d) {
                case '|':
                    consume();
                    token = newToken(LOGICAL_OR, "||");
                    break loop;
                case '=':
                    consume();
                    token = newToken(BITWISE_OR_EQUALS, "|=");
                    break loop;
                }
                token = newToken(BITWISE_OR, "|");
                break loop;
            case '&':
                consume();
                d = la();
                switch (d) {
                case '&':
                    consume();
                    token = newToken(LOGICAL_AND, "&&");
                    break loop;
                case '=':
                    consume();
                    token = newToken(BITWISE_AND_EQUALS, "&&");
                    break loop;
                }
                token = newToken(BITWISE_AND, "&");
                break loop;
            case '^':
                consume();
                if (la() == '=') {
                    consume();
                    token = newToken(BITWISE_XOR_EQUALS, "^=");
                    break loop;
                }
                token = newToken(BITWISE_XOR, "^");
                break loop;
            case '~':
                consume();
                token = newToken(INVERSION, "~");
                break loop;
            case '\u0009':
                consume();
                // return newToken(TAB, "\u0009");
                continue loop;
            case '\u000B':
                consume();
                // return newToken(VERTICAL_TAB, "\u000B");
                continue loop;
            case '\u000C':
                consume();
                // return newToken(FORM_FEED, "\u000C");
                continue loop;
            case '\u0020':
                consume();
                // return newToken(SPACE, "\u0020");
                continue loop;
            case '\u00A0':
                consume();
                // return newToken(NOBREAK_SPACE, "\u00A0");
                continue loop;
            case '\uFEFF':
                consume();
                // return newToken(BYTE_ORDER_MARK, "\uFEFF");
                continue loop;
            case '\n':
                consume();
                token = newToken(NL, "\n");
                incrementLine();
                break loop;
            case '\r':
                consume();
                if (la() == '\n') {
                    consume();
                    token = newToken(CRNL, "\r\n");
                } else {
                    token = newToken(CR, "\r");
                }
                incrementLine();
                break loop;
            case '\u2028':
                consume();
                token = newToken(LINE_SEPARATOR, "\u2028");
                break loop;
            case '\u2029':
                consume();
                token = newToken(PARAGRAPH_SEPARATOR, "\u2029");
                break loop;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                token = numericLiteral();
                break loop;
            case '\'':
                token = stringLiteral('\'');
                break loop;
            case '"':
                token = stringLiteral('"');
                break loop;
            }

            if (Character.getType(c) == Character.SPACE_SEPARATOR) {
                consume();
                continue loop;
            }

            if (Character.isLetter(c) || c == '$' || c == '_' || isUnicodeEscapeSequence(c)) {
                token = identifierOrReservedWord();
                if (token != null) {
                    break loop;
                }
            }

            throw new SyntaxError("unexpected character: " + c);
        }

        return token;
    }

    private boolean isIdentifierStart(char c) {
        return (Character.isLetter(c) || Character.getType(c) == Character.LETTER_NUMBER || c == '$' || c == '_' || (isUnicodeEscapeSequence(c) && !isNonEscapeSequence(c)));
    }

    private boolean isIdentifierPart(char c) {
        if (isIdentifierStart(c)) {
            return true;
        }

        int type = Character.getType(c);
        switch (type) {
        case Character.DECIMAL_DIGIT_NUMBER:
        case Character.COMBINING_SPACING_MARK:
        case Character.NON_SPACING_MARK:
        case Character.CONNECTOR_PUNCTUATION:
            return true;
        }
        return false;
    }

    private boolean isUnicodeEscapeSequence(char start) {
        if (start != '\\') {
            return false;
        }

        return (la(2) == 'u' && (isHexDigit(la(3)) && isHexDigit(la(4)) && isHexDigit(la(5)) && isHexDigit(la(6))));
    }

    private boolean isNonEscapeSequence(char start) {
        if (la(3) == '0' && la(4) == '0' && la(5) == '0' && (la(6) == 'A' || la(6) == 'D')) {
            return true;
        }
        if (la(3) == '2' && la(4) == '0' && la(5) == '2' && (la(6) == '8' || la(6) == '9')) {
            return true;
        }
        return false;
    }

    private boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    protected Token regexpLiteral() {
        StringBuffer text = new StringBuffer();
        text.append("/");

        while (la() != '/' && la() != 0) {
            switch (la()) {
            case '[':
                text.append(consume());
                while (la() != ']') {
                    switch (la()) {
                    case '\\':
                        text.append(consume());
                        text.append(consume());
                        break;
                    case '[':
                        // Java doesn't allow unescaped "[" inside "["
                        text.append('\\');
                        text.append(consume());
                        break;
                    default:
                        text.append(consume());
                    }
                }
                text.append(consume());
                break;
            case '\\':
                text.append(consume());
                text.append(consume());
                break;
            default:
                text.append(consume());
            }
        }

        text.append(consume());

        while (isIdentifierPart(la())) {
            text.append(consume());
        }

        return newToken(REGEXP_LITERAL, text.toString());
    }

    protected Token identifierOrReservedWord() {
        StringBuffer text = new StringBuffer();

        if (isIdentifierStart(la())) {
            text.append(consume());
        }

        while (isIdentifierPart(la())) {
            text.append(consume());
        }

        if (text.length() == 0) {
            isNonEscapeSequence(la());
            consume();
            consume();
            consume();
            consume();
            consume();
            consume();
            // return null;
            throw new SyntaxError("unicode escapes not allowed here");
        }

        String str = text.toString();

        if (KEYWORDS.contains(str)) {
            return newToken(TokenType.valueOf(str.toUpperCase()), str);
        }

        return newToken(IDENTIFIER, text.toString());
    }

    protected boolean isLineTerminator(char c) {
        return c == '\n' || c == '\r' || c == '\u2028' || c == '\u2029';
    }

    protected void singleLineComment() {
        consume();
        char c = 0;
        while (true) {
            c = consume();
            if (c == '\r' && la() == '\n') {
                consume();
                break;
            } else if (c == 0 || c == '\r' || c == '\n' || c == '\u2028' || c == '\u2029') {
                break;
            }
        }
        incrementLine();
    }

    protected void multiLineComment() {
        consume();

        while (true) {
            char c = consume();
            if (c == 0) {
                throw new LexerException("unexpected end-of-file");
            } else if (c == '\n') {
                incrementLine();
            } else if (c == '\r') {
                if (la() == '\n') {
                    consume();
                }
                incrementLine();
            } else if (c == '*' && la() == '/') {
                consume();
                return;
            }
        }
    }

    protected Token numericLiteral() {
        char c = la();

        if (c == '0') {
            char d = la(2);
            if (d == 'x' || d == 'X') {
                return hexLiteral();
            } else if (d >= '0' && d <= '7') {
                return octalLiteral();
            }
        }

        return decimalLiteral();
    }

    protected Token octalLiteral() {
        StringBuffer text = new StringBuffer();
        consume(); // 0

        while (la() >= '0' && la() <= '7') {
            text.append(consume());
        }

        return newToken(OCTAL_LITERAL, text.toString());
    }

    protected Token decimalLiteral() {
        StringBuffer text = new StringBuffer();

        char c = la();
        if (c == '+' || c == '-') {
            text.append(consume());
        }

        while (true) {
            c = la();
            if (c >= '0' && c <= '9') {
                text.append(consume());
            } else {
                break;
            }
        }

        if (la() == '.') {
            text.append(consume());
            while (true) {
                c = la();
                if (c >= '0' && c <= '9') {
                    text.append(consume());
                } else {
                    break;
                }
            }
        }

        if (la() == 'E' || la() == 'e') {
            text.append(consume());
            c = la();
            if (c == '+' || c == '-') {
                text.append(consume());
            }

            while (true) {
                c = la();
                if (c >= '0' && c <= '9') {
                    text.append(consume());
                } else {
                    break;
                }
            }
        }

        return newToken(DECIMAL_LITERAL, text.toString());
    }

    protected Token hexLiteral() {
        StringBuffer text = new StringBuffer();

        text.append(consume()); // 0
        text.append(consume()); // x

        while (true) {
            char c = la();
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                text.append(consume());
            } else {
                break;
            }
        }

        return newToken(HEX_LITERAL, text.toString());
    }

    protected Token stringLiteral(char type) {
        StringBuffer text = new StringBuffer();
        consume();

        char c = 0;

        boolean escapedString = false;
        boolean escapedOctalString = false;

        while ((c = la()) != type) {
            if (c == 0) {
                throw new LexerException("unexpected end-of-file");
            }
            if (c == '\n') {
                throw new LexerException("line-feeds not allowed within string literals");
            }
            if (c == '\r') {
                throw new LexerException("carriage-returns not allowed within string literals");
            }
            if (c == '\\') {
                char d = la(2);
                switch (d) {
                case '\'':
                case '"':
                case '\\':
                    consume();
                    text.append(consume());
                    break;
                case 'b':
                    consume();
                    consume();
                    text.append("\b");
                    break;
                case 'f':
                    consume();
                    consume();
                    text.append("\f");
                    break;
                case 'n':
                    consume();
                    consume();
                    text.append("\n");
                    break;
                case 'r':
                    consume();
                    consume();
                    text.append("\r");
                    break;
                case 't':
                    consume();
                    consume();
                    text.append("\t");
                    break;
                case 'v':
                    consume();
                    consume();
                    text.append("\u000B");
                    break;
                case '\n':
                case '\r':
                case '\u2028':
                case '\u2029':
                    consume();
                    lineTerminatorSequence();
                    break;
                case 'u':
                    text.append(unicodeEscapeSequence());
                    escapedString = true;
                    break;
                case 'x':
                    text.append(hexEscapeSequence());
                    escapedString = true;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    text.append(octalEscapeSequence());
                    escapedString = true;
                    escapedOctalString = true;
                    break;
                default:
                    consume();
                    text.append(consume());
                }
            } else {
                text.append(consume());
            }
        }

        consume();

        Token token = newToken(STRING_LITERAL, text.toString());
        token.setEscapedString(escapedString);
        token.setEscapedOctalString(escapedOctalString);
        return token;
    }

    protected String unicodeEscapeSequence() {
        StringBuffer text = new StringBuffer();
        text.append("0x");
        consume();
        consume();

        for (int i = 0; i < 4; ++i) {
            char c = hexDigit();
            text.append(c);
        }

        int code = Integer.decode(text.toString());
        return new String(Character.toChars(code));
    }

    protected String hexEscapeSequence() {
        StringBuffer text = new StringBuffer();
        text.append("0x");
        consume(); // \
        consume(); // x

        for (int i = 0; i < 2; ++i) {
            text.append(hexDigit());
        }

        int code = Integer.decode(text.toString());
        return Character.toString((char) code);
    }

    protected String octalEscapeSequence() {
        StringBuffer text = new StringBuffer();
        consume(); // \

        text.append(octalDigit());

        if (isOctalDigit(la())) {
            text.append(octalDigit());

            if (isOctalDigit(la())) {
                text.append(octalDigit());
            }
        }

        return text.toString();
    }

    protected boolean isOctalDigit(char c) {
        if (c >= '0' && c <= '7') {
            return true;
        }

        return false;
    }

    protected char octalDigit() {
        char c = la();
        if (isOctalDigit(c)) {
            return consume();
        }

        throw new LexerException("expected octal digit, but found '" + c + "'");
    }

    protected char hexDigit() {
        char c = la();
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
            return consume();
        }

        throw new LexerException("expected hex digit, but found '" + c + "'");
    }

    protected void lineTerminatorSequence() {
        char c = la();

        switch (c) {
        case '\n':
        case '\u2028':
        case '\u2029':
            consume();
            return;
        case '\r':
            consume();
            if (la(2) == '\n') {
                consume();
                return;
            }
            return;
        }
    }
}
