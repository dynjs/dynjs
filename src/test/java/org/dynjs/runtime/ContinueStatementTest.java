package org.dynjs.runtime;

import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class ContinueStatementTest extends AbstractDynJSTestSupport {

    @Test(expected=ThrowException.class)
    public void testIllegalContinue() {
        eval( "continue;" );
    }
}