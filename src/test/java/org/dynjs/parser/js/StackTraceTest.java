package org.dynjs.parser.js;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.fest.assertions.Assertions.*;

/**
 * @author Bob McWhirter
 */
public class StackTraceTest extends AbstractDynJSTestSupport {

    @Test
    public void testStackCreationAccuracy() {
        try {
            Object result = eval(getClass().getResourceAsStream("/stack.js"));
            fail( "should have thrown" );
        } catch (ThrowException e) {
            StackTraceElement[] stack = e.getStackTrace();
            assertThat( stack[0].getLineNumber() ).isEqualTo( 13 );
        }
    }
}
