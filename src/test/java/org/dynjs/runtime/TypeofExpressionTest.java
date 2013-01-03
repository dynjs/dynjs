package org.dynjs.runtime;

import org.junit.Test;

public class TypeofExpressionTest extends AbstractDynJSTestSupport {

    @Test
    public void testTypeofStringLiteral() {
        System.err.println( eval( "typeof Date()" ) );
    }
    
}