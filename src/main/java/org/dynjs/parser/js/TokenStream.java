package org.dynjs.parser.js;

public interface TokenStream {

    Token peekToken();

    TokenType peek();

    Token peekToken(int pos);

    TokenType peek(int pos);

    Token consume();

    Token peekToken(boolean skipSkippable);

    TokenType peek(boolean skipSkippable);

    Token peekToken(boolean skipSkippable, int pos);

    TokenType peek(boolean skipSkippable, int pos);

    Token consume(boolean skipSkippable);

}
