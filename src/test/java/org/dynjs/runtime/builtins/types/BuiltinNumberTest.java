package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.junit.Test;

public class BuiltinNumberTest extends AbstractDynJSTestSupport {

    @Test
    public void testNumberPositiveInfinity() {
        assertThat(eval("Number.POSITIVE_INFINITY")).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNumberNegativeInfinity() {
        assertThat(eval("Number.NEGATIVE_INFINITY")).isEqualTo(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testMinValue() {
        Object minValue = eval("Number.MIN_VALUE");
        assertThat(minValue).isEqualTo(Double.MIN_VALUE);
    }

    @Test
    public void testMaxValue() {
        Object minValue = eval("Number.MAX_VALUE");
        assertThat(minValue).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    public void testNumberPrototypeNotNull() {
        check("var result = (Number.prototype == null)", false);
    }

    @Test
    public void testPositiveZero() {
        check("var result = +0", 0L);
    }

    @Test
    public void testNegativeZero() {
        check("var result = -0", -0.0);
    }

    @Test
    public void testNegativeWholeNumbers() {
        check("var result = -123", -123L);
    }

    @Test
    public void testPositiveWholeNumbers() {
        check("var result = +123", 123L);
    }

    @Test
    public void testNumberFunction() {
        this.runtime.execute("var x = Number(8)");
        Reference result = this.runtime.getDefaultExecutionContext().resolve("x");
        Long value = (Long) result.getValue(getContext());
        assertThat(value).isEqualTo(8L);
    }

    @Test
    public void testNumberCtor() {
        this.runtime.execute("var x = new Number(33)");
        Reference result = this.runtime.getDefaultExecutionContext().resolve("x");
        PrimitiveDynObject value = (PrimitiveDynObject) result.getValue(getContext());
        assertThat(value.getPrimitiveValue()).isEqualTo(33L);
    }

    @Test
    public void testNumberPrototypeConstructor() {
        // 15.7.1
        this.runtime.execute("var x = Number.prototype.constructor");
        Reference result = this.runtime.getDefaultExecutionContext().resolve("x");
        assertThat(result.getValue(getContext())).isInstanceOf(BuiltinNumber.class);
    }

    @Test
    public void testNumberDefault() {
        assertThat(eval("new Number().valueOf()")).isEqualTo(0L);
    }

    @Test
    public void testNumberPrototypeToString() {
        // 15.7.2
        check("var result = Number.prototype.toString()", "0");
    }

    @Test
    public void testNumberToString() {
        // 15.7.2
        check("var result = new Number(12); result = result.toString()", "12");
    }
    
    @Test
    public void testFloatToString() {
        eval("var x = 3.123;");
        assertThat(eval("x.toString();")).isEqualTo("3.123");
    }

    @Test
    public void testIntToString() {
        eval("var x = 3;");
        assertThat(eval("x.toString();")).isEqualTo("3");
    }

    @Test(expected = ThrowException.class)
    public void testNumberToStringNotOnNumber() {
        eval("var s1 = new String(); s1.toString = Number.prototype.toString; s1.toString()");
        fail("Number.toString() on non-Number should throw TypeError");
    }

    @Test(expected = ThrowException.class)
    public void testNumberPrototypeToStringWithNullRadix() {
        eval("Number.prototype.toString(null)");
        fail("Number.prototype.toString(null) should throw RangeError");
    }

    @Test(expected = ThrowException.class)
    public void testNumberPrototypeToStringWithZeroRadix() {
        eval("Number.prototype.toString(0)");
        fail("Number.prototype.toString(0) should throw RangeError");
    }

    @Test
    public void testNumberToStringWithNonTenRadix() {
        Object result = eval( "Number(255).toString(16)" );
        assertThat( result ).isEqualTo("ff");
    }

    @Test
    public void testNumberToStringWithNoRadix() {
        Object result = eval( "Number(255).toString()" );
        assertThat( result ).isEqualTo( "255" );
    }

    @Test
    public void testNumberPrototypeToLocaleString() {
        // 15.7.3
        check("var result = Number.prototype.toLocaleString()", "0");
    }

    @Test
    public void testNumberToLocaleString() {
        // 15.7.3
        check("var result = new Number(12); result = result.toLocaleString()", "12");
    }

    @Test
    public void testNumberPrototypeValueOf() {
        // 15.7.4
        check("var result = Number.prototype.valueOf()", 0L);
    }

    @Test
    public void testNewNumberValueOf() {
        // 15.7.4
        check("var result = new Number(12).valueOf();", 12L);
    }

    @Test
    public void testNumberValueOf() {
        // 15.7.4
        assertThat(eval("Number(12).valueOf()")).isEqualTo(12L);
    }

    @Test
    public void testNaNValueOf() {
        // 15.7.4
        assertThat(eval("NaN.valueOf()")).isEqualTo(Double.NaN);
    }

    @Test
    public void testNewNumberNaNValueOf() {
        // 15.7.4
        check("var result = new Number('adf').valueOf();", Double.NaN);
    }

    @Test
    public void testNumberNaNValueOf() {
        // 15.7.4
        check("var result = Number.NaN.valueOf();", Double.NaN);
    }

    @Test
    public void testNumberPositiveInfinityValueOf() {
        // 15.7.4
        check("var result = Number.POSITIVE_INFINITY.valueOf()", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNumberNegativeInfinityValueOf() {
        // 15.7.4
        check("var result = Number.NEGATIVE_INFINITY.valueOf()", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testPositiveInfinityValueOf() {
        // 15.7.4
        check("var result = Infinity.valueOf()", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNegativeInfinityValueOf() {
        // 15.7.4
        check("var result = -Infinity.valueOf()", Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testExponentValueOf() {
        // 15.7.4 TODO: The check value should really be 1000 - an int
        check("var result = new Number(1E3).valueOf();", 1000.0);
    }

    @Test
    public void testNumberToFixedDefault() {
        check("var result = new Number(12345.67890123).toFixed()", "12346");
    }

    @Test
    public void testToFixedWithString() {
        check("var result = (Number(1e21).toFixed() === String(1e21))", true);
    }

    @Test
    public void testNumberToFixedWithTooManyParameters() {
        check("var result = new Number(123).toFixed(1,2,3)", Types.UNDEFINED);
    }

    @Test
    public void testNumberNaNToFixedWithDouble() {
        check("result = (Number.NaN.toFixed(0.9) === 'NaN')", true);
    }

    @Test
    public void testBigAssNumberToFixed() {
        assertThat(eval("new Number(1e+21).toFixed()")).isEqualTo("1e+21");
    }

    @Test
    public void testBigAssButSmallerNumberToFixed() {
        check("var result = new Number(1E+20).toFixed();", "100000000000000000000");
    }

    @Test
    public void testRoundedFractionalDigitsToFixed() {
        check("var result = new Number(13.45).toFixed(1)", "13.4");
    }

    @Test
    public void testBigAssJavaFriendlyNumberToFixed() {
        check("var result = new Number(1.0E+21).toFixed();", "1e+21");
    }

    @Test
    public void testNumberNegativeDigitsRangeError() {
        try {
            check("var result = new Number(123).toFixed(-1)", "0");
            fail("toFixed() should throw RangeError with -1 as a parameter");
        } catch (ThrowException error) {
            assertThat(((JSObject) error.getValue()).get(getContext(), "name")).isEqualTo("RangeError");
        }
    }

    @Test
    public void testNumberTooManyDigitsRangeError() {
        try {
            check("var result = new Number(123).toFixed(21)", "0");
            fail("toFixed() should throw RangeError with 21 as a parameter");
        } catch (ThrowException error) {
            assertThat(((JSObject) error.getValue()).get(getContext(), "name")).isEqualTo("RangeError");
        }
    }

    @Test
    public void testNumberNaNToFixed() {
        check("var result = Number.NaN.toFixed()", "NaN");
    }

    @Test
    public void testNumberPositiveInfinityToFixed() {
        check("var result = Number.POSITIVE_INFINITY.toFixed()", "Infinity");
    }

    @Test
    public void testNumberCtorToNaNToFixed() {
        check("var result = new Number('adf').toFixed()", "NaN");
    }

    @Test
    public void testNegativeNumberToFixed() {
        check("var result = new Number(-12).toFixed()", "-12");
    }

    @Test
    public void testNumberCtorLength() {
        check("var result = Number.length", 1L);
    }

    @Test
    public void testToFixedLength() {
        check("var result = new Number(12).toFixed.length", 1L);
    }

    @Test
    public void testToFixedWithPrecision() {
        check("var result = new Number(12.12345).toFixed(2)", "12.12");
    }

    @Test
    public void testToExponentialLength() {
        check("var result = new Number(21).toExponential.length", 1L);
    }

    @Test
    public void testToExponential() {
        check("var result = new Number(144).toExponential()", "1.44e+2");
    }

    @Test
    public void testNegativeNumberToExponential() {
        check("var result = new Number(-14).toExponential()", "-1.4e+1");
    }

    @Test
    public void testToExponentialNaN() {
        check("var result = new Number('asdf').toExponential()", "NaN");
    }

    @Test
    public void testZeroToExponential() {
        check("var result = new Number(0).toExponential()", "0e+0");
    }

    @Test
    public void testDoubleZeroToExponential() {
        check("var result = new Number(0.0).toExponential()", "0e+0");
    }

    @Test(expected = ThrowException.class)
    public void testNumberValueOfNotOnNumber() {
        eval("var s1 = new String(); s1.valueOf = Number.prototype.valueOf; s1.valueOf()");
        fail("Number.valueOf() on non-Number should throw TypeError");
    }

    @Test
    public void testDivideByZeroIsPositiveInfinity() {
        check("var result = (1/0 == Infinity)", true);
    }

    @Test
    public void testDivideByZeroIsStrictlyPositiveInfinity() {
        check("var result = (1/0 === Infinity)", true);
    }

    @Test
    public void testNumberPrototypeIsZero() {
        check("var result = 1/Number.prototype", Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFunctionWithHexZero() {
        assertThat(eval("Number('0X0')")).isEqualTo(0L);
    }

    @Test
    public void testFunctionWithUnicodeStr() {
        eval("Number('\\u0009\\u000C\\u0020\\u00A0\\u000B\\u000A\\u000D\\u2028\\u2029\\u1680\\u180E\\u2000\\u2001\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200A\\u202F\\u205F\\u3000')");
    }

    @Test
    public void testToPrecision() {
        assertThat(eval("NaN.toPrecision()")).isEqualTo("NaN");
        assertThat(eval("Number(13.3714).toPrecision()")).isEqualTo("13.3714");
        assertThat(eval("Number(13.3714).toPrecision(2)")).isEqualTo("13");
        assertThat(eval("Number(13.3714).toPrecision(3)")).isEqualTo("13.4");
        assertThat(eval("Number(13.3714).toPrecision(10)")).isEqualTo("13.37140000");
    }

    @Test
    public void testDivisionByZero() {
        assertThat(eval("1/Number(0)")).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testDivisionByNegativeZero() {
        assertThat(eval("1/Number(-0)")).isEqualTo(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testInfinityStrictEquality() {
        assertThat(eval("Infinity === Number.POSITIVE_INFINITY")).isEqualTo(true);
    }

    @Test
    public void testNumberFunctionIsZero() {
        assertThat(eval("Number() === 0")).isEqualTo(true);
    }
    
    @Test
    public void testParseFloat() {
        Object result = eval( "1/parseFloat(-0)");
        
        System.err.println( "result: " + result + " // " + result.getClass() );
        
        
        
        
        
    }

}
