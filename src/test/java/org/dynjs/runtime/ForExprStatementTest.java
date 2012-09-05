package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ForExprStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicLoop() {
        eval("var y=0;",
                "var i;",
                "for ( i = 0 ; i < 10; ++i ) {",
                "  y = i;",
                "}");

        Object i = getContext().resolve("i").getValue(getContext());
        assertThat(i).isEqualTo(10);

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(9);
    }

    @Test
    public void testWithoutInitializer() {
        eval("var y=0;",
                "var i=0;",
                "for ( ; i < 10; ++i ) {",
                "  y = i;",
                "}");

        Object i = getContext().resolve("i").getValue(getContext());
        assertThat(i).isEqualTo(10);

        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(y).isEqualTo(9);
    }
}
