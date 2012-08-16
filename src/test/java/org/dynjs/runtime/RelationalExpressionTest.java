package org.dynjs.runtime;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class RelationalExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testLessThan() {
        assertThat(eval("1 < 2")).isEqualTo(true);
        assertThat(eval("2 < 1")).isEqualTo(false);
        assertThat(eval("2 < 2")).isEqualTo(false);
        assertThat(eval("'bob' < 'bobmcw'")).isEqualTo(true);
        assertThat(eval("'bobmcw' < 'bob'")).isEqualTo(false);
    }

    @Test
    public void testGreaterThan() {
        assertThat(eval("1 > 2")).isEqualTo(false);
        assertThat(eval("2 > 1")).isEqualTo(true);
        assertThat(eval("2 > 2")).isEqualTo(false);
        assertThat(eval("'bob' > 'bobmcw'")).isEqualTo(false);
        assertThat(eval("'bobmcw' > 'bob'")).isEqualTo(true);
    }

    @Test
    public void testLessThanEqual() {
        assertThat(eval("1 <= 2")).isEqualTo(true);
        assertThat(eval("2 <= 1")).isEqualTo(false);
        assertThat(eval("2 <= 2")).isEqualTo(true);
    }

    @Test
    public void testGreaterThanEqual() {
        assertThat(eval("1 > 2")).isEqualTo(false);
        assertThat(eval("2 > 1")).isEqualTo(true);
        assertThat(eval("2 >= 2")).isEqualTo(true);
    }

}
