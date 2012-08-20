package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractDynJSTestSupport;
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
        check("var result = Number(8)", 8);
    }
    
    @Test
    public void testNumberCtor() {
        check("var result = new Number(33);", 33);
        // calling result = result.valueOf() - which is what we should be doing here
        // results in IncompatibleClassChangeError
    }
}
