package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class TypesTest extends AbstractDynJSTestSupport {

    @Test
    public void testDecimalStringToNumber() {
        assertThat( Types.toNumber( getContext(), "4242" ) ).isEqualTo(4242L);
    }
    
    @Test
    public void testHexStringToNumber() {
        assertThat( Types.toNumber( getContext(), "0xFF" ) ).isEqualTo( 255L );
    }

    @Test
    public void testNull() {
        assertThat( Types.NULL == new Types.Null() );
    }
}
