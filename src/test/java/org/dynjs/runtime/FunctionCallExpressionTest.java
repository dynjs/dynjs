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

    @Test
    public void testAnotherValidFunction() {
        Object result = eval("(function(){return})();");
        assertThat(result).isEqualTo(Types.UNDEFINED);
    }

    @Test(expected = ThrowException.class)
    public void testUncallable() {
        eval("var x = {}; x();");
    }

    @Test
    public void testFunctionWithoutReturn() {
        Object result = eval( "var f = function(){ 42; };",
                "f()" );
        assertThat( result ).isEqualTo( Types.UNDEFINED );
                
    }
}