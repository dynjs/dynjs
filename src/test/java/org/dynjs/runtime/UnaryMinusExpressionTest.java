package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class UnaryMinusExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testUnaryMinusInt() {
        Object result = eval("-42");
        assertThat(result).isEqualTo(-42);
    }

    @Test
    public void testUnaryMinusDouble() {
        Object result = eval("-42.2");
        assertThat(result).isEqualTo(-42.2);
    }

    @Test
    public void testUnaryMinusOnVar() {
        Object result = eval("var x = 1; -x;");
        System.err.println(result);
    }

}
