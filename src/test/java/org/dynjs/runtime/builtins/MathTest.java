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
        assertPrimitive("Math.LOG2E", java.lang.Math.log(java.lang.Math.E) / java.lang.Math.log(2));
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
        assertThat(eval("Math.asin(0.5)")).isEqualTo(java.lang.Math.asin(0.5));
    }

    @Test
    public void testMathAsinNaN() {
        assertThat(eval("Math.asin(new Number('qw4'))")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAsinGreaterThanOne() {
        assertThat(eval("Math.asin(1.5)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAsinLessThanNegativeOne() {
        assertThat(eval("Math.asin(-1.5)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAsinNegativeZero() {
        assertThat(eval("Math.asin(-0)")).isEqualTo(0);
    }

    @Test
    public void testMathAsinZero() {
        assertThat(eval("Math.asin(0)")).isEqualTo(0);
    }

    @Test
    public void testMathAsinFloatyZero() {
        assertThat(eval("Math.asin(0.0)")).isEqualTo(0);
    }

    @Test
    public void testMathAtan() {
        assertThat(eval("Math.atan(0.5)")).isEqualTo(java.lang.Math.atan(0.5));
    }

    @Test
    public void testAtanNaN() {
        assertThat(eval("Math.atan(new Number('asdf'))")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAtanPositiveZero() {
        assertThat(eval("Math.atan(0)")).isEqualTo(0);
    }

    @Test
    public void testMathAtanNegativeZero() {
        assertThat(eval("Math.atan(-0)")).isEqualTo(0);
    }

    @Test
    public void testMathAtanNegativeInfinity() {
        assertThat(eval("Math.atan(-Infinity)")).isEqualTo(-java.lang.Math.PI / 2);
    }

    @Test
    public void testMathAtanPositiveInfinity() {
        assertThat(eval("Math.atan(Infinity)")).isEqualTo(java.lang.Math.PI / 2);
    }

    @Test
    public void testMathAtan2Length() {
        assertThat(eval("Math.atan2.length")).isEqualTo(2);
    }

    @Test
    public void testMathAtan2WithOnlyOneArg() {
        assertThat(eval("Math.atan2(0.4)")).isEqualTo(Double.NaN);
    }

    @Test
    public void testMathAtan2() {
        assertThat(eval("Math.atan2(0.5, 0.5)")).isEqualTo(java.lang.Math.atan2(0.5, 0.5));
    }

    @Test
    public void testMathAtan2WithIntegers() {
        assertThat(eval("Math.atan2(1, 1)")).isEqualTo(java.lang.Math.atan2(1, 1));
    }

    @Test
    public void testMathAtan2WithXInteger() {
        assertThat(eval("Math.atan2(1.0, 1)")).isEqualTo(java.lang.Math.atan2(1.0, 1));
    }

    @Test
    public void testMathAtan2YNaN() {
        assertEval("Math.atan2(new Number('asdf'), 0.3)", Double.NaN);
    }

    @Test
    public void testMathAtan2XNaN() {
        assertEval("Math.atan2(0.3, new Number('asdf'))", Double.NaN);
    }

    @Test
    public void testMathAtan2XGreaterThanZero() {
        assertEval("Math.atan2(1, 0)", java.lang.Math.PI / 2);
    }

    @Test
    public void testMathAtan2NegativeInfinity() {
        assertEval("Math.atan2(-Infinity, Number.NEGATIVE_INFINITY)", -3 * java.lang.Math.PI / 4);
    }

    @Test
    public void testMathCeil() {
        assertEval("Math.ceil(12)", 12);
    }

    @Test
    public void testMathCeilFloaty() {
        assertEval("Math.ceil(12.12)", 13);
    }

    @Test
    public void testMathCeilNaN() {
        assertEval("Math.ceil(new Number('asdf'))", Double.NaN);
    }

    @Test
    public void testMathCeilPositiveInfinity() {
        assertEval("Math.ceil(Number.POSITIVE_INFINITY)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathCeilNegativeInfinity() {
        assertEval("Math.ceil(Number.NEGATIVE_INFINITY)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathCeilNegativeLessThanNegativeOne() {
        assertEval("Math.ceil(-0.2)", 0);
    }

    @Test
    public void testMathCos() {
        assertEval("Math.cos(3)", java.lang.Math.cos(3));
    }

    @Test
    public void testMathCosZero() {
        assertEval("Math.cos(0)", 1);
    }

    @Test
    public void testMathCosFloaty() {
        assertEval("Math.cos(3.5)", java.lang.Math.cos(3.5));
    }

    @Test
    public void testMathCosInfinite() {
        assertEval("Math.cos(Infinity)", Double.NaN);
    }

    @Test
    public void testMathCosNegativeInfinite() {
        assertEval("Math.cos(-Infinity)", Double.NaN);
    }

    @Test
    public void testMathExp() {
        assertEval("Math.exp(123.456)", java.lang.Math.exp(123.456));
    }

    @Test
    public void testMathExpInt() {
        assertEval("Math.exp(123)", java.lang.Math.exp(123));
    }

    @Test
    public void testMathExpZero() {
        assertEval("Math.exp(0)", 1);
    }

    @Test
    public void testMathExpInfinite() {
        assertEval("Math.exp(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathExpNegativeInfinite() {
        assertEval("Math.exp(-Infinity)", 0);
    }

    @Test
    public void testMathFloor() {
        assertEval("Math.floor(1.6)", 1);
    }

    @Test
    public void testMathFloorInfinite() {
        assertEval("Math.floor(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathFloorNegativeInfinite() {
        assertEval("Math.floor(-Infinity)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathFloorFootnote() {
        assertEval("Math.floor(1.234) == -Math.ceil(-1.234)", true);
    }

    @Test
    public void testMathFloorGreaterThanZeroButLessThanOne() {
        assertEval("Math.floor(0.99999999)", 0);
    }

    @Test
    public void testMathLog() {
        assertEval("Math.log(2)", java.lang.Math.log(2));
    }

    @Test
    public void testMathLogNaN() {
        assertEval("Math.log(new Number('asdf'))", Double.NaN);
    }

    @Test
    public void testMathLogLessThanZero() {
        assertEval("Math.log(-1)", Double.NaN);
    }

    @Test
    public void testMathLogZero() {
        assertEval("Math.log(0)", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathLogExactlyOne() {
        assertEval("Math.log(1)", 0);
    }

    @Test
    public void testMathLogInfinity() {
        assertEval("Math.log(Infinity)", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathMaxLength() {
        assertEval("Math.max.length", 2);
    }

    @Test
    public void testMathMax() {
        assertEval("Math.max(0.5, 2)", 2);
    }

    @Test
    public void testMathMaxNoArgs() {
        assertEval("Math.max()", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMathMaxOneArg() {
        assertEval("Math.max(2)", 2);
    }

    @Test
    public void testMathMaxThreeArgs() {
        assertEval("Math.max(1,4,6)", 6);
    }

    @Test
    public void testMathMaxVeryManyArgs() {
        assertEval("Math.max(1,4,6,12,4,987,0,12.34,98765.45)", 98765.45);
    }

    @Test
    public void testMathMaxVeryManyArgsAndOneNaNStuckInTheMiddle() {
        assertEval("Math.max(1,4,6,12,4,987,Number.NaN,12.34,98765.45)", Double.NaN);
    }

    @Test
    public void testMathMinLength() {
        assertEval("Math.min.length", 2);
    }

    @Test
    public void testMathMin() {
        assertEval("Math.min(0.5, 2)", 0.5);
    }

    @Test
    public void testMathMinNoArgs() {
        assertEval("Math.min()", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testMathMinOneArg() {
        assertEval("Math.min(2)", 2);
    }

    @Test
    public void testMathMinThreeArgs() {
        assertEval("Math.min(1,4,0.5)", 0.5);
    }

    @Test
    public void testMathMinVeryManyArgs() {
        assertEval("Math.min(1,4,6,12,4,987,0,12.34,98765.45)", 0);
    }

    @Test
    public void testMathMinVeryManyArgsAndOneNaNStuckInTheMiddle() {
        assertEval("Math.min(1,4,6,12,4,987,Number.NaN,12.34,98765.45)", Double.NaN);
    }

    @Test
    public void testMathPow() {
        assertEval("Math.pow(12,12.123)", java.lang.Math.pow(12, 12.123));
    }
    
    @Test
    public void testMathPowWithYNaN() {
        assertEval("Math.pow(12, NaN)", Double.NaN);
    }
    
    @Test
    public void testMathPowXNaNYZero() {
        assertEval("Math.pow(NaN, 0)", 1);
    }
    
    @Test
    public void testMathRandom() {
        assertEval("Math.random() > 0", true);
    }

    @Test
    public void testMathRandomLessThanOne() {
        assertEval("Math.random() < 1", true);
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

    private void assertEval(String javascript, Object expected) {
        assertThat(eval(javascript)).isEqualTo(expected);
    }

    private void assertPrimitive(String javascript, Number value) {
        final Object result = eval(javascript);
        assertThat(result).isInstanceOf(DynNumber.class);
        assertThat(((DynNumber) result).getPrimitiveValue()).isEqualTo(value);
    }
}
