package org.dynjs.parser.js;

import static org.fest.assertions.Assertions.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class CiruclarCharBufferTest {
    
    private StringReader reader;
    private CircularCharBuffer buf;

    @Before
    public void setUp() throws IOException {
        reader = new StringReader( "0123456789");
        buf = new CircularCharBuffer( reader, 5 );
    }
    
    @Test
    public void testPeek() throws Exception {
        assertThat( buf.peek() ).isEqualTo( '0' );
        assertThat( buf.peek(1) ).isEqualTo( '0' );
        assertThat( buf.peek(2) ).isEqualTo( '1' );
        assertThat( buf.peek(3) ).isEqualTo( '2' );
    }
    
    @Test
    public void testWithConsume() throws Exception {
        assertThat( buf.peek() ).isEqualTo('0');
        assertThat( buf.consume() ).isEqualTo('0');
        assertThat( buf.peek() ).isEqualTo('1');
        assertThat( buf.consume() ).isEqualTo('1');
        assertThat( buf.peek() ).isEqualTo('2');
        assertThat( buf.consume() ).isEqualTo('2');
        assertThat( buf.peek() ).isEqualTo('3');
        assertThat( buf.consume() ).isEqualTo('3');
        assertThat( buf.peek() ).isEqualTo('4');
        assertThat( buf.consume() ).isEqualTo('4');
    }
    
    @Test
    public void testAvailable() throws Exception {
        assertThat( buf.available() ).isEqualTo( 5 );
        assertThat( buf.consume() ).isEqualTo('0');
        assertThat( buf.available() ).isEqualTo( 4 );
        assertThat( buf.consume() ).isEqualTo('1');
        assertThat( buf.available() ).isEqualTo( 3 );
        assertThat( buf.consume() ).isEqualTo('2');
        assertThat( buf.available() ).isEqualTo( 2 );
        assertThat( buf.consume() ).isEqualTo('3');
        assertThat( buf.available() ).isEqualTo( 1 );
        assertThat( buf.consume() ).isEqualTo('4');
        assertThat( buf.available() ).isEqualTo( 0 );
        
    }
    
    @Test
    public void testWithConsumeWithFill() throws Exception {
        assertThat( buf.available() ).isEqualTo( 5 );
        assertThat( buf.peek() ).isEqualTo('0');
        assertThat( buf.consume() ).isEqualTo('0');
        assertThat( buf.available() ).isEqualTo( 4 );
        assertThat( buf.peek() ).isEqualTo('1');
        assertThat( buf.consume() ).isEqualTo('1');
        assertThat( buf.available() ).isEqualTo( 3 );
        assertThat( buf.peek() ).isEqualTo('2');
        assertThat( buf.consume() ).isEqualTo('2');
        assertThat( buf.available() ).isEqualTo( 2 );
        assertThat( buf.peek() ).isEqualTo('3');
        assertThat( buf.consume() ).isEqualTo('3');
        assertThat( buf.available() ).isEqualTo( 1 );
        assertThat( buf.peek() ).isEqualTo('4');
        assertThat( buf.consume() ).isEqualTo('4');
        assertThat( buf.available() ).isEqualTo( 0 );
        assertThat( buf.peek() ).isEqualTo('5');
        assertThat( buf.consume() ).isEqualTo('5');
        assertThat( buf.available() ).isEqualTo( 4 );
        assertThat( buf.peek() ).isEqualTo('6');
        assertThat( buf.consume() ).isEqualTo('6');
    }

}


