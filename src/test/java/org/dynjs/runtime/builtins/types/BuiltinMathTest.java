package org.dynjs.runtime.builtins.types;
import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.junit.Test;

public class BuiltinMathTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testMathPrototypeIsUnefined() {
        assertThat(eval("Math.prototype")).isEqualTo(Types.UNDEFINED);
    }
    
    @Test
    public void testMathE() {
        assertPrimitive("Math.E", Math.E);
    }
    
    @Test
    public void testLn10() {
        assertPrimitive("Math.LN10", Math.log(10));
    }

    @Test
    public void testLn2() {
        assertPrimitive("Math.LN2", Math.log(2));
    }

    @Test
    public void testLog2e() {
        assertPrimitive("Math.LOG2E", Math.log(Math.E)/Math.log(2));
    }
    
    @Test
    public void testLog10e() {
        assertPrimitive("Math.LOG10E", Math.log10(Math.E));
    }

    @Test
    public void testMathPi() {
        assertPrimitive("Math.PI", Math.PI);
    }
    
    @Test
    public void testMathSqrt1_2() {
        assertPrimitive("Math.SQRT1_2", Math.sqrt(0.5f));
    }

    @Test
    public void testMathSqrt2() {
        assertPrimitive("Math.SQRT2", Math.sqrt(2.0f));
    }

    private void assertPrimitive(String javascript, Number value) {
        final Object result = eval(javascript);
        assertThat(result).isInstanceOf(DynNumber.class);
        assertThat(((DynNumber)result).getPrimitiveValue()).isEqualTo(value);
    }
}
