package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class BitwiseShiftTest extends AbstractDynJSTestSupport {

    @Test
    public void testShiftLeftBoundary() {
    	Object result = eval( "-2147483648 << 0" );
    	assertThat( result ).isEqualTo( -2147483648 );
    }

}
