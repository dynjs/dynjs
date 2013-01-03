package org.dynjs.parser.js;

import java.util.LinkedList;
import java.util.ListIterator;

public class TokenQueue implements TokenStream {

    private Lexer lexer;
    private LinkedList<Token> queue = new LinkedList<>();

    public TokenQueue(Lexer lexer) {
        this.lexer = lexer;
    }

    public Token peekToken() {
        return peekToken(true, 1);
    }

    @Override
    public TokenType peek() {
        return peek(true, 1);
    }

    public Token peekToken(int pos) {
        return peekToken(true, pos);
    }

    @Override
    public TokenType peek(int pos) {
        return peek(true, pos);
    }

    @Override
    public Token consume() {
        return consume(true);
    }

    public Token peekToken(boolean skipSkippable) {
        return peekToken(skipSkippable, 1);
    }

    @Override
    public TokenType peek(boolean skipSkippable) {
        return peek(skipSkippable, 1);
    }

    public Token peekToken(boolean skipSkippable, int pos) {
        int count = 0;
        for (Token each : queue) {
            if (skipSkippable && each.isSkippable()) {
                continue;
            }
            ++count;
            if (count == pos) {
                return each;
            }
        }

        while (true) {
            Token nextToken = lexer.nextToken();
            queue.add(nextToken);
            if (skipSkippable && nextToken.isSkippable()) {
                continue;
            }
            ++count;
            if (count == pos) {
                return nextToken;
            }
        }
    }

    @Override
    public TokenType peek(boolean skipSkippable, int pos) {
        return peekToken(skipSkippable, pos).getType();
    }

    @Override
    public Token consume(boolean skipSkippable) {
        ListIterator<Token> iter = queue.listIterator();
        while (iter.hasNext()) {
            Token each = iter.next();
            iter.remove();
            if (skipSkippable && each.isSkippable()) {
                continue;
            }
            return each;
        }

        while (true) {
            Token nextToken = lexer.nextToken();
            if (skipSkippable && nextToken.isSkippable()) {
                continue;
            }
            return nextToken;
        }
    }

}
