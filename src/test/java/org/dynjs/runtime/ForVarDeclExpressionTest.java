package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ForVarDeclExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        eval("var y=0;",
                "for ( var i = 0 ; i < 10; ++i ) {",
                "  y = i;",
                "}");

        Object i = getContext().resolve("i").getValue(getContext());
        assertThat(i).isEqualTo(10);

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(9);
    }

    @Test
    public void testLoopWithNaNTest() {
        Object result = eval("var y=0;",
                "for ( var i = 0 ; NaN; ) {",
                "  y = i;",
                "}",
                "y");

        assertThat(result).isEqualTo(0);
    }
}
