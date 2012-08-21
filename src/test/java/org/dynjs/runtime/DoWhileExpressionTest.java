package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class DoWhileExpressionTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testBasicDoWhile() {
        eval( "var x = 0",
                "do {",
                "  x = 42;",
                "} while ( x != 42 );");
        Object x = getContext().resolve( "x" ).getValue(getContext());
        assertThat( x ).isEqualTo( 42 );
    }
    
    @Test
    public void testDoWhileWithoutCurlies() {
        eval( "var x = 0",
                "do x = 42; while ( x != 42 );");
        Object x = getContext().resolve( "x" ).getValue(getContext());
        assertThat( x ).isEqualTo( 42 );
    }
    
    @Test
    public void testDoWhileWithTestThrowing() {
        eval( "var x = 0",
                "do x = 42; while ( iDoNotExist );");
        Object x = getContext().resolve( "x" ).getValue(getContext());
        assertThat( x ).isEqualTo( 42 );
    }

}
