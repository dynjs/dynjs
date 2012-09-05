package org.dynjs.runtime;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class WithStatementTest extends AbstractDynJSTestSupport {

    @Test
    public void testBasicWith() {
        Object result = eval("var x = { fish: 'taco' };",
                "with(x) {",
                "  cheese='cheddar'",
                "  fish;",
                "}");

        assertThat(result).isEqualTo("taco");
        assertThat(eval("x.cheese")).isEqualTo(Types.UNDEFINED);

    }
}