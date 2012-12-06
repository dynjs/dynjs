package org.dynjs.parser.js;

import static org.dynjs.parser.js.TokenType.*;
import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class JSLexerTest {

    protected Token lex(String str) throws IOException {
        StringReader reader = new StringReader(str);
        CircularCharBuffer buf = new CircularCharBuffer(reader, 5);
        Lexer lexer = new Lexer(buf);
        Token t = lexer.nextToken();
        return t;
    }

    @Test
    public void testPunctuation() throws IOException {
        assertThat(lex("{").getType()).isEqualTo(LEFT_BRACE);
        assertThat(lex("}").getType()).isEqualTo(RIGHT_BRACE);
        assertThat(lex("(").getType()).isEqualTo(LEFT_PAREN);
        assertThat(lex(")").getType()).isEqualTo(RIGHT_PAREN);
        assertThat(lex("[").getType()).isEqualTo(LEFT_BRACKET);
        assertThat(lex("]").getType()).isEqualTo(RIGHT_BRACKET);

        assertThat(lex(".").getType()).isEqualTo(DOT);
        assertThat(lex(";").getType()).isEqualTo(SEMICOLON);
        assertThat(lex(",").getType()).isEqualTo(COMMA);
        assertThat(lex(":").getType()).isEqualTo(COLON);
        assertThat(lex("?").getType()).isEqualTo(QUESTION);

        assertThat(lex("<").getType()).isEqualTo(LESS_THAN);
        assertThat(lex("<=").getType()).isEqualTo(LESS_THAN_EQUAL);
        assertThat(lex("<<").getType()).isEqualTo(LEFT_SHIFT);
        assertThat(lex("<<=").getType()).isEqualTo(LEFT_SHIFT_EQUALS);
        assertThat(lex(">").getType()).isEqualTo(GREATER_THAN);
        assertThat(lex(">=").getType()).isEqualTo(GREATER_THAN_EQUAL);
        assertThat(lex(">>").getType()).isEqualTo(RIGHT_SHIFT);
        assertThat(lex(">>>").getType()).isEqualTo(UNSIGNED_RIGHT_SHIFT);
        assertThat(lex(">>=").getType()).isEqualTo(RIGHT_SHIFT_EQUALS);
        assertThat(lex(">>>=").getType()).isEqualTo(UNSIGNED_RIGHT_SHIFT_EQUALS);

        assertThat(lex("&").getType()).isEqualTo(BITWISE_AND);
        assertThat(lex("&=").getType()).isEqualTo(BITWISE_AND_EQUALS);
        assertThat(lex("&&").getType()).isEqualTo(LOGICAL_AND);

        assertThat(lex("|").getType()).isEqualTo(BITWISE_OR);
        assertThat(lex("|=").getType()).isEqualTo(BITWISE_OR_EQUALS);
        assertThat(lex("||").getType()).isEqualTo(LOGICAL_OR);

        assertThat(lex("^").getType()).isEqualTo(BITWISE_XOR);
        assertThat(lex("^=").getType()).isEqualTo(BITWISE_XOR_EQUALS);

        assertThat(lex("~").getType()).isEqualTo(INVERSION);

        assertThat(lex("+").getType()).isEqualTo(PLUS);
        assertThat(lex("+=").getType()).isEqualTo(PLUS_EQUALS);
        assertThat(lex("++").getType()).isEqualTo(PLUS_PLUS);

        assertThat(lex("-").getType()).isEqualTo(MINUS);
        assertThat(lex("-=").getType()).isEqualTo(MINUS_EQUALS);
        assertThat(lex("--").getType()).isEqualTo(MINUS_MINUS);

        assertThat(lex("*").getType()).isEqualTo(MULTIPLY);
        assertThat(lex("*=").getType()).isEqualTo(MULTIPLY_EQUALS);

        //assertThat(lex("/").getType()).isEqualTo(DIVIDE);
        assertThat(lex("/=").getType()).isEqualTo(DIVIDE_EQUALS);

        assertThat(lex("%").getType()).isEqualTo(MODULO);
        assertThat(lex("%=").getType()).isEqualTo(MODULO_EQUALS);
    }
    
    @Test
    public void testIdentifiers() throws Exception {
        assertThat(lex("foo+bar").getType()).isEqualTo(IDENTIFIER);
        assertThat(lex("foo+bar").getText()).isEqualTo("foo");
    }

    @Test
    public void testKeywords() throws Exception {
        assertThat(lex("while").getType()).isEqualTo(WHILE);
        assertThat(lex("function").getType()).isEqualTo(FUNCTION);
    }
    
    @Test
    public void testBooleanLiterals() throws Exception {
        assertThat(lex("true").getType()).isEqualTo(TRUE);
        assertThat(lex("false").getType()).isEqualTo(FALSE);
    }
    
    @Test
    public void testNumericLiterals() throws Exception {
        assertThat( lex( "42" ).getType() ).isEqualTo(DECIMAL_LITERAL);
        assertThat( lex( "42" ).getText() ).isEqualTo("42");
        
        assertThat( lex( "42.02" ).getType() ).isEqualTo(DECIMAL_LITERAL);
        assertThat( lex( "42.02" ).getText() ).isEqualTo("42.02");
        
        assertThat( lex( ".02" ).getType() ).isEqualTo(DECIMAL_LITERAL);
        assertThat( lex( ".02" ).getText() ).isEqualTo(".02");
        
        assertThat( lex( "42.02e+23" ).getType() ).isEqualTo(DECIMAL_LITERAL);
        assertThat( lex( "42.02e+23" ).getText() ).isEqualTo("42.02e+23");
        
        assertThat( lex( "0xDECAFBAD" ).getType() ).isEqualTo(HEX_LITERAL);
        assertThat( lex( "0xDECAFBAD" ).getText() ).isEqualTo("0xDECAFBAD");
    }
    
    @Test
    public void testStringLiterals() throws Exception {
        assertThat( lex( "'taco'" ).getType() ).isEqualTo( STRING_LITERAL );
        assertThat( lex( "'taco'" ).getText() ).isEqualTo( "taco" );
        
        assertThat( lex( "\"taco\"" ).getType() ).isEqualTo( STRING_LITERAL );
        assertThat( lex( "'taco'" ).getText() ).isEqualTo( "taco" );
        
        assertThat( lex( "'taco\\\nwith cheese'" ).getType() ).isEqualTo( STRING_LITERAL );
        assertThat( lex( "'taco\\\nwith cheese'" ).getText() ).isEqualTo( "tacowith cheese" );
    }
    

}
