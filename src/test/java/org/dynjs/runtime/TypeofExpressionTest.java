package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class TypeofExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testTypeofStringLiteral() {
        System.err.println( eval( "typeof Date()" ) );
    }
    
}