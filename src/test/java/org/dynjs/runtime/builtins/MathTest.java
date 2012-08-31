package org.dynjs.runtime.builtins;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.number.DynNumber;
import org.junit.Test;

public class MathTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testMathPrototypeIsUndefined() {
        assertThat(eval("Math.prototype")).isEqualTo(Types.UNDEFINED);
    }
    
    @Test
    public void testMathE() {
        assertPrimitive("Math.E", java.lang.Math.E);
    }
    
    @Test
    public void testLn10() {
        assertPrimitive("Math.LN10", java.lang.Math.log(10));
    }

    @Test
    public void testLn2() {
        assertPrimitive("Math.LN2", java.lang.Math.log(2));
    }

    @Test
    public void testLog2e() {
        assertPrimitive("Math.LOG2E", java.lang.Math.log(java.lang.Math.E)/java.lang.Math.log(2));
    }
    
    @Test
    public void testLog10e() {
        assertPrimitive("Math.LOG10E", java.lang.Math.log10(java.lang.Math.E));
    }

    @Test
    public void testMathPi() {
        assertPrimitive("Math.PI", java.lang.Math.PI);
    }
    
    @Test
    public void testMathSqrt1_2() {
        assertPrimitive("Math.SQRT1_2", java.lang.Math.sqrt(0.5f));
    }

    @Test
    public void testMathSqrt2() {
        assertPrimitive("Math.SQRT2", java.lang.Math.sqrt(2.0f));
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
        assertThat(eval("Math.acos(0.5)")).isEqualTo(java.lang.Math.acos(0.5));
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
    public void testMathAsin() {
        assertThat(eval("Math.asin(0.5)")).isEqualTo( java.lang.Math.asin( 0.5 ) );
    }
    
    @Test
    public void testMathAsinNaN() {
        assertThat(eval("Math.asin(new Number('qw4'))")).isEqualTo( Double.NaN );
    }
    
    @Test
    public void testMathAsinGreaterThanOne() {
        assertThat(eval("Math.asin(1.5)")).isEqualTo( Double.NaN );
    }
    
    @Test
    public void testMathAsinLessThanNegativeOne() {
        assertThat(eval("Math.asin(-1.5)")).isEqualTo( Double.NaN );
    }
    
    @Test
    public void testMathAsinNegativeZero() {
        assertThat(eval("Math.asin(-0)")).isEqualTo( 0 );
    }
    
    @Test
    public void testMathAsinZero() {
        assertThat(eval("Math.asin(0)")).isEqualTo( 0 );
    }
    
    @Test
    public void testMathAsinFloatyZero() {
        assertThat(eval("Math.asin(0.0)")).isEqualTo( 0 );
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
