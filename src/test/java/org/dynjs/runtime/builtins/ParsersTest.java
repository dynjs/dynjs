package org.dynjs.runtime.builtins;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

public class ParsersTest extends AbstractDynJSTestSupport {

    @Test
    public void testNoArgReturnInsideIf() {
        eval( "var f = function(){\n",
                "if ( true ) {\n",
                "return\n",
                "}\n",
                "}\n" );
    }

    @Test
    public void testNoArgReturnInsideIfWithElseNoBraces() {
        eval( "var f = function(){\n",
                "if ( true ) \n",
                "  return\n",
                "else\n",
                "  return\n",
                "}\n" );
    }

    @Test
    public void parsesFloats() {
        check("var result = parseFloat('33.2');", 33.2);
    }
    
    @Test
    public void parsesIntWithUnicodes() {
        check("var result = parseInt('\u00A01')", 1L);
    }
    
    @Test
    public void parsesFloatWithUnicodeNBSP() {
        check("var result = parseFloat('\u00A01.1')", 1.1);        
    }
    
    @Test
    public void parsesFloatWithUnicodeLineBreak() {
        // This seems to break the parser
        check("var result = parseFloat(\"\u20281.1\")", 1.1);        
    }
    
    @Test
    public void parseIntForcesHex() {
        check("var result = parseInt('0x1', 0)", 1L);
    }
    
    @Test
    public void parseIntWithBase2() {
        check("var result = parseInt('0123456789', 2)", 1L);
    }
    
    @Test
    public void parseIntWithBase6() {
        check("var result = parseInt('01234567890', 6)", 1865L);
    }
    
    @Test
    public void parseFloatReturnsNaNForEmptyString() {
        check("var result = parseFloat('')", Double.NaN);
    }
    
    @Test
    public void parseFloatWorksWithBuiltinNumber() {
        check("var result = parseFloat(new Number(-1.1))", -1.1);
    }
    
    @Test
    public void parseFloatWorksWithBuiltinString() {
        check("var result = parseFloat(new String('-1.1'))", -1.1);
    }
    
    @Test
    public void parseIntReturnsNaNForUndefined() {
        check("var result = parseInt(undefined)", Double.NaN);
    }
    
    @Test
    public void parseIntReturnsNaN() {
        check("var result = parseInt(true)", Double.NaN);
    }

    @Test
    public void parseIntReturnsNaNForEmptyString() {
        check("var result = parseInt('')", Double.NaN);
    }
    
    @Test
    public void parseIntWorksWithBuiltinNumber() {
        check("var result = parseInt(new Number(-1))", -1L);
    }
    
    @Test
    public void parseIntWorksWithBuiltinString() {
        check("var result = parseInt(new String('-1'))", -1L);
    }
    
    @Test
    public void parseFloatReturnsNaNForUndefined() {
        check("var result = parseFloat(undefined)", Double.NaN);
    }
    
    @Test
    public void parseFloatReturnsNaN() {
        check("var result = parseFloat(true)", Double.NaN);
    }

    @Test
    public void parsesInts() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('32');", 32L);
    }
    
    @Test( expected = ThrowException.class )
    public void parseIntCalledAsConstructor() {
        eval("new parseInt()");
    }

    @Test( expected = ThrowException.class )
    public void parseFloatCalledAsConstructor() {
        eval("new parseFloat()");
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
        check("var result = parseInt('-32');", -32L);
    }

    @Test
    public void parseIntHandlesLeadingWhiteSpace() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt(' 32');", 32L);
    }

    @Test
    public void parseIntHandlesTrailingWhiteSpace() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('32 ');", 32L);
    }

    @Test
    public void parseIntHandlesHexValues() {
        // http://es5.github.com/#x15.1.2.2
        check("var result = parseInt('0x32', 16);", 50L);
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
        check("var result = parseInt('32', 0);", 32L);
    }

    @Test
    public void parseIntIgnoresMoreThanTwoParameters() {
        check("var result = parseInt('123', 10, 20);", 123L);
    }

    @Test
    public void parseIntReturnsTheFloorOfAFloat() {
        check("var result = parseInt('12.6');", 12L);
    }
    
    @Test
    public void parseIntWithInfiniteRadix() {
        check("var result = parseInt('11', Infinity)", 11L);
    }
    
    @Test
    public void parseIntWithBoolean() {
        check("var result = parseInt(true)", Double.NaN);
    }
    
    @Test
    public void parseIntWithInteger() {
        check("var result = parseInt(-1)", -1L);
    }
    
    @Test
    public void parseIntWithInfinity() {
        check("var result = parseInt(Infinity)", Double.NaN);
    }
}
