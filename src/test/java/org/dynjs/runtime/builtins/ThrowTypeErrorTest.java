package org.dynjs.runtime.builtins;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSObject;
import org.junit.Test;

public class ThrowTypeErrorTest extends AbstractDynJSTestSupport {

    @Test
    public void testThrowTypeError() {
        try {
            eval("__throwTypeError()");
            throw new AssertionError("Should have thrown a type error" );
        } catch (ThrowException e) {
            JSObject err = (JSObject) e.getValue();
            assertThat( err.get( getContext(), "name" ) ).isEqualTo("TypeError" );
        }
    }
}