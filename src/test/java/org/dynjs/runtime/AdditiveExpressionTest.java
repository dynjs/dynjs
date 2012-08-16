package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class AdditiveExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testAddIntegers() {
        Object result = eval( "1+2" );
        assertThat( result ).isEqualTo( 3 );
    }

    @Test
    public void testAddDoubles() {
        Object result = eval( "1.1 + 2.1" );
        assertThat( result ).isEqualTo( 3.2 );
    }

    @Test
    public void testAddIntToDouble() {
        Object result = eval( "1 + 2.1" );
        assertThat( result ).isEqualTo( 3.1 );
    }

    @Test
    public void testAddDoubleToInt() {
        Object result = eval( "1.1 + 2" );
        assertThat( result ).isEqualTo( 3.1 );
    }

    @Test
    public void testSubtractIntegers() {
        Object result = eval( "3-1" );
        assertThat( result ).isEqualTo( 2 );
    }

    @Test
    public void testSubtractDoubles() {
        Object result = eval( "3.2 - 1.1" );
        assertThat( result ).isEqualTo( 2.1 );
    }
    
    @Test
    public void testSubtractIntFromDouble() {
        Object result = eval( "3.2 - 1" );
        assertThat( result ).isEqualTo(  2.2 );
    }
    
    @Test
    public void testSubtractDoubleFromInt() {
        Object result = eval( "3 - 1.1" );
        assertThat( result ).isEqualTo( 1.9 );
    }
}
