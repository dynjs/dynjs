package org.dynjs.runtime.builtins.types;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.junit.Test;

public class BuiltinMathTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testMathPrototypeIsUndefined() {
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
    
    @Test
    public void testMathAbs() {
        assertThat(eval("Math.abs(-2)")).isEqualTo(2);
    }
    
    @Test
    public void testMathAbsNaN() {
        assertThat(eval("Math.abs(new Number('asdf'))")).isEqualTo(Double.NaN);
    }
    
    @Test
    public void testMathAbsNegativeInfinity() {
        assertThat(eval("Math.abs(-Infinity)")).isEqualTo(Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testMathAbsNumberNegativeInfinity() {
        assertThat(eval("Math.abs(Number.NEGATIVE_INFINITY)")).isEqualTo(Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testMathAbsNegativeZero() {
        assertThat(eval("Math.abs(-0)")).isEqualTo(0);
    }
    
    @Test
    public void testMathAcos() {
        assertThat(eval("Math.acos(0.5)")).isEqualTo(Math.acos(0.5));
    }
    
    @Test
    public void testMathAcosNaN() {
        assertThat(eval("Math.acos(new Number('asdf'))")).isEqualTo(Double.NaN);
    }
    
    @Test
    public void testMathAcosGreaterThanOne() {
        assertThat(eval("Math.acos(1.5)")).isEqualTo(Double.NaN);
    }
    
    @Test
    public void testMathAcosLessThanNegativeOne() {
        assertThat(eval("Math.acos(-1.5)")).isEqualTo(Double.NaN);
    }
    
    @Test
    public void testMathAcosExactlyOne() {
        assertThat(eval("Math.acos(1)")).isEqualTo(0);
    }
    
@Test
    public void testMathConstructor() {
        try {
            eval("new Math()");
            fail("The Math constructor should raise a TypeError");
        } catch (ThrowException e) {
            // expected
        }
    }

    @Test
    public void testMathFunction() {
        try {
            eval("Math()");
            fail("The Math function should raise a TypeError");
        } catch (ThrowException e) {
            // expected
        }
    }

    private void assertPrimitive(String javascript, Number value) {
        final Object result = eval(javascript);
        assertThat(result).isInstanceOf(DynNumber.class);
        assertThat(((DynNumber)result).getPrimitiveValue()).isEqualTo(value);
    }
}
