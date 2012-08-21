package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.builtins.types.BuiltinNumber;
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
}
