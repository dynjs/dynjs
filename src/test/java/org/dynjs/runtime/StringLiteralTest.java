package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class StringLiteralTest extends AbstractDynJSTestSupport {

    @Test
    public void testEmptyObjectCreation() {
        eval( "var x = 'howdy';" );
        Reference x = getContext().resolve( "x" );
        assertThat( x ).isNotNull();
        assertThat( x.getValue( getContext() ) ).isEqualTo( "howdy" );
    }
}
