package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class UnaryMinusExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testUnaryMinusInt() {
        Object result = eval("-42");
        assertThat(result).isEqualTo(-42L);
    }

    @Test
    public void testUnaryMinusDouble() {
        Object result = eval("-42.2");
        assertThat(result).isEqualTo(-42.2);
    }

    @Test
    public void testUnaryMinusOnVar() {
        Object result = eval("var x = 1; -x;");
        assertThat(result).isEqualTo(-1L);
    }
    
    @Test
    public void testNegativeZero() {
        assertThat( eval( "-0" ) ).isEqualTo( -0.0 );
    }
    
    @Test
    public void testNegativeNumberZero() {
        assertThat( eval( "-Number(0)" ) ).isEqualTo( -0.0 );
    }
    
    @Test
    public void testNegativeNumberZeroString() {
        assertThat( eval( "-Number('0')" ) ).isEqualTo( -0.0 );
    }
    
    @Test
    public void testNegativeNumberZeroString2() {
        assertThat( eval( "Number('-0')" ) ).isEqualTo( -0.0 );
    }


}
