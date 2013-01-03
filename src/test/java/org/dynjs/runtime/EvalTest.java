package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class EvalTest extends AbstractDynJSTestSupport {

    @Test
    public void testValidFunction() {
        Object result = eval("eval('var x=42');x" );
        assertThat( result ).isEqualTo(42L);
    }

}