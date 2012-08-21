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
        this.runtime.execute("var x = Number(8)", null, 0);
        Reference result = this.runtime.getExecutionContext().resolve("x");
        PrimitiveDynObject value = (PrimitiveDynObject) result.getValue(getContext());
        assertThat(value.getPrimitiveValue()).isEqualTo(8);
    }

    @Test
    public void testNumberCtor() {
        this.runtime.execute("var x = new Number(33)", null, 0);
        Reference result = this.runtime.getExecutionContext().resolve("x");
        PrimitiveDynObject value = (PrimitiveDynObject) result.getValue(getContext());
        assertThat(value.getPrimitiveValue()).isEqualTo(33);
    }
}
