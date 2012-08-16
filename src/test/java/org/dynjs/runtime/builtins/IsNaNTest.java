package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

public class IsNaNTest extends AbstractDynJSTestSupport {

    @Test
    public void isNaNReturnsFalseForNull() {
        check( "var result = isNaN(null);", false );
    }

    @Test
    public void isNaNReturnsFalseForBooleanTrue() {
        check( "var result = isNaN(true);", false );
    }

    @Test
    public void isNaNReturnsFalseForBooleanFalse() {
        check( "var result = isNaN(false);", false );
    }

    @Test
    public void isNaNReturnsFalseForEmptyString() {
        check( "var result = isNaN('');", false );
    }

    @Test
    public void isNaNReturnsFalseForWhiteSpaceString() {
        check( "var result = isNaN(' ');", false );
    }

    @Test
    public void isNaNReturnsFalseForIntegerString() {
        check( "var result = isNaN('+12');", false );
    }

    @Test
    public void isNaNReturnsFalseNegativeIntegerString() {
        check( "var result = isNaN('-12');", false );
    }

    @Test
    public void isNaNReturnsFalseForFloatString() {
        check( "var result = isNaN('12.123');", false );
    }

    @Test
    public void isNaNReturnsFalseForHexString() {
        check( "var result = isNaN('0x16');", false );
    }

    @Test
    public void isNaNReturnsTrueForCharacterString() {
        check( "var result = isNaN('abc');", true );
    }

}
