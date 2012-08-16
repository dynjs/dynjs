package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiplicativeExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testMultiplyIntegers() {
        Object result = eval("2*4");
        assertThat(result).isEqualTo(8);
    }

    @Test
    public void testMultiplyDoubles() {
        Object result = eval("1.1 * 2.0");
        assertThat(result).isEqualTo(2.2);
    }

    @Test
    public void testMultiplyIntAndDouble() {
        Object result = eval("2 * 2.1");
        assertThat(result).isEqualTo(4.2);
    }

    @Test
    public void testMultiplyDoubleAndInt() {
        Object result = eval("2.1 * 2");
        assertThat(result).isEqualTo(4.2);
    }

    //

    @Test
    public void testDivideIntegers() {
        Double result = (Double) eval("8/3");
        assertEquals(2.6, result, 0.1);
    }

    @Test
    public void testDivideDoubles() {
        Double result = (Double) eval("9.92 / 3.1 ");
        assertEquals(3.2, result, 0.1);
    }

    @Test
    public void testDivideIntegerByDouble() {
        Double result = (Double) eval("20 / 2.3");
        assertEquals(8.69, result, 0.1);
    }

    @Test
    public void testDivideDoubleByInteger() {
        Double result = (Double) eval("8.4 / 2 ");
        assertEquals(4.2, result, 0.1);
    }

}
