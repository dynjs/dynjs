package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class JavaIntegrationTest extends AbstractDynJSTestSupport {

    @Test
    public void testImplementAnInterface() {
        Object result = eval(
                "var x = 1;",
                "new java.lang.Runnable( {",
                "  run: function() { ",
                "    x = 42;",
                "  }",
                "} )");

        assertThat(result).isInstanceOf(Runnable.class);

        Object x = eval("x");
        assertThat(x).isEqualTo(1L);

        Runnable runnable = (Runnable) result;
        runnable.run();
        
        x = eval("x");
        assertThat(x).isEqualTo(42L);

    }
}
