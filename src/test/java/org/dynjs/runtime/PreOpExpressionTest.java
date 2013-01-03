package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class PreOpExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testPreIncrementInteger() {
        eval("var x = 1; var y = ++x;");
        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(x).isEqualTo(2L);
        assertThat(y).isEqualTo(2L);
    }

    @Test
    public void testPreDecrementInteger() {
        eval("var x = 2; var y = --x;");
        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(x).isEqualTo(1L);
        assertThat(y).isEqualTo(1L);
    }

    @Test
    public void testPreIncrementDouble() {
        eval("var x = 1.5; var y = ++x;");
        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(x).isEqualTo(2.5);
        assertThat(y).isEqualTo(2.5);
    }

    @Test
    public void testPreDecrementDouble() {
        eval("var x = 2.5; var y = --x;");
        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(x).isEqualTo(1.5);
        assertThat(y).isEqualTo(1.5);
    }

}
