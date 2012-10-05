package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.exception.ThrowException;
import org.junit.Ignore;
import org.junit.Test;

public class ContinueStatementTest extends AbstractDynJSTestSupport {

    @Test(expected=ThrowException.class)
    public void testIllegalContinue() {
        eval( "continue;" );
    }
}