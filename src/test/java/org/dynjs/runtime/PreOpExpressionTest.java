package org.dynjs.runtime;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class PreOpExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testPreIncrementInteger() {
        eval("var x = 1; var y = ++x;");
        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(x).isEqualTo(2);
        assertThat(y).isEqualTo(2);
    }

    @Test
    public void testPreDecrementInteger() {
        eval("var x = 2; var y = --x;");
        Object x = getContext().resolve("x").getValue(getContext());
        Object y = getContext().resolve("y").getValue(getContext());
        assertThat(x).isEqualTo(1);
        assertThat(y).isEqualTo(1);
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
