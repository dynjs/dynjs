package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class BitwiseInversionOperatorTest extends AbstractDynJSTestSupport {

    @Test
    public void testInversion() {
    	assertThat( eval( "~20" ) ).isEqualTo( eval( "~20" ));
    }

}
