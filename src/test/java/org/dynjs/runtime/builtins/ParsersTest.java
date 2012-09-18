package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

public class ParsersTest extends AbstractDynJSTestSupport {

    @Test
    public void parsesFloats() {
        check("var result = parseFloat('33.2');", 33.2);
    }

    @Test
    public void parsesInts() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('32');", 32);
    }

    @Test
    public void parseIntHandlesNaNValues() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('biscuit');", Double.NaN);
    }

    @Test
    public void parseIntHandlesEmptyString() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('');", Double.NaN);
    }

    @Test
    public void parseIntHandlesNegativeValues() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('-32');", -32);
    }

    @Test
    public void parseIntHandlesLeadingWhiteSpace() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt(' 32');", 32);
    }

    @Test
    public void parseIntHandlesTrailingWhiteSpace() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('32 ');", 32);
    }

    @Test
    public void parseIntHandlesHexValues() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('0x32', 16);", 50);
    }

    @Test
    public void parseIntHandlesInvalidRadix() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('32', 38);", Double.NaN);
    }

    @Test
    public void parseIntHandlesInvalidRadixWithHex() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('0x32', 38);", Double.NaN);
    }

    @Test
    public void parseIntHandlesRadixOfZero() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('32', 0);", 32);
    }

    @Test
    public void parseIntIgnoresMoreThanTwoParameters() {
        check("var result = parseInt('123', 10, 20);", 123);
    }

    @Test
    public void parseIntReturnsTheFloorOfAFloat() {
        check("var result = parseInt('12.6');", 12);
    }
    
    @Test
    public void parseIntWithInfiniteRadix() {
        check("var result = parseInt('11', Infinity)", 11);
    }
}
