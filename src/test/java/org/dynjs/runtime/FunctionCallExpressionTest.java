package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class FunctionCallExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testValidFunction() {
        Object result = eval("var x = function(){ return 42;};",
                "x()");
        assertThat(result).isEqualTo(42L);
    }

    @Test(expected = ThrowException.class)
    public void testUncallable() {
        eval("var x = {}; x();");
    }

}