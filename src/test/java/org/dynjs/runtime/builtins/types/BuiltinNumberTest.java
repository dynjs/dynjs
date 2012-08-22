package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import org.dynjs.exception.RangeError;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.builtins.types.BuiltinNumber;
import org.junit.Ignore;
import org.junit.Test;

public class BuiltinNumberTest extends AbstractDynJSTestSupport {

    @Test
    public void testPositiveInfinity() {
        check("var result = Number.POSITIVE_INFINITY", BuiltinNumber.POSITIVE_INFINITY);
    }

    @Test
    public void testNegativeInfinity() {
        check("var result = Number.NEGATIVE_INFINITY", BuiltinNumber.NEGATIVE_INFINITY);
    }

    @Test
    public void testNumberPrototypeNotNull() {
        check("var result = (Number.prototype == null)", false);
    }

    @Test
    public void testPositiveZero() {
        check("var result = +0", 0);
    }

    @Test
    public void testNegativeZero() {
        check("var result = -0", 0);
    }

    @Test
    public void testNegativeWholeNumbers() {
        check("var result = -123", -123);
    }

    @Test
    public void testPositiveWholeNumbers() {
        check("var result = +123", 123);
    }

    @Test
    public void testNumberFunction() {
        this.runtime.execute("var x = Number(8)");
        Reference result = this.runtime.getExecutionContext().resolve("x");
        PrimitiveDynObject value = (PrimitiveDynObject) result.getValue(getContext());
        assertThat(value.getPrimitiveValue()).isEqualTo(8);
    }

    @Test
    public void testNumberCtor() {
        this.runtime.execute("var x = new Number(33)");
        Reference result = this.runtime.getExecutionContext().resolve("x");
        PrimitiveDynObject value = (PrimitiveDynObject) result.getValue(getContext());
        assertThat(value.getPrimitiveValue()).isEqualTo(33);
    }
    
    @Test
    public void testNumberPrototypeConstructor() {
        // 15.7.1
        this.runtime.execute("var x = Number.prototype.constructor");
        Reference result = this.runtime.getExecutionContext().resolve("x");
        assertThat(result.getValue(getContext())).isInstanceOf(BuiltinNumber.class);
    }
    
    @Test
    public void testNumberPrototypeToString() {
        // 15.7.2
        check("var result = Number.prototype.toString()", "0");
    }
    
    @Test
    public void testNumberToString() {
        check("var result = new Number(12); result = result.toString()", "12");
    }
    
    @Test
    public void testNumberPrototypeToLocaleString() {
        // 15.7.3
        check("var result = Number.prototype.toLocaleString()", "0");
    }

    @Test
    public void testNumberToLocaleString() {
        check("var result = new Number(12); result = result.toLocaleString()", "12");
    }
    
    @Test
    public void testNumberPrototypeValueOf() {
        // 15.7.4
        check("var result = Number.prototype.valueOf()", 0);
    }

    @Test
    public void testNumberValueOf() {
        check("var result = new Number(12).valueOf();", 12);
    }
    
    @Test
    public void testNumberToFixedDefault() {
        check("var result = new Number(12345.67890123).toFixed()", "12345.67890123");
    }
    
    @Test
    public void testNumberNegativeDigitsRangeError() {
        try {
            check("var result = new Number(123).toFixed(-1)", "0");
            fail("toFixed() should throw RangeError with -1 as a parameter");
        } catch (RangeError error) {
            // expected
        } catch (Exception e) {
            fail("Unexpected exception thrown. " + e);
        }
    }

    @Test
    public void testNumberTooManyDigitsRangeError() {
        try {
            check("var result = new Number(123).toFixed(21)", "0");
            fail("toFixed() should throw RangeError with 21 as a parameter");
        } catch (RangeError error) {
            // expected
        } catch (Exception e) {
            fail("Unexpected exception thrown. " + e);
        }
    }
    
    @Ignore
    public void testNumberToFixedNaN() {
        // TODO: Make globalObject.NaN a DynObject
        check("var result = Number.NaN.toFixed()", "NaN");
    }
    
    @Test
    public void testNumberCtorNaNToFixed() {
        check("var result = new Number('adf').toFixed()", "NaN");
    }
    
    @Test
    public void testNegativeNumberToFixed() {
        check("var result = new Number(-12).toFixed()", "-12");
    }
    
    @Ignore
    public void testToFixedLength() {
        // TODO: what's up with this?
        check("var result = new Number(12).toFixed.length", 1);
    }
    
    @Ignore
    public void testToFixedWithPrecision() {
        check("var result = new Number(12.12345).toFixed(2)", 12.12);
    }
}
