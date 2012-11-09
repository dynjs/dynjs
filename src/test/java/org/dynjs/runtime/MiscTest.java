package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.exception.ThrowException;
import org.junit.Test;

public class MiscTest extends AbstractDynJSTestSupport {

    @Test(expected=ThrowException.class)
    public void testInvalidStatement_in() {
        Object result = eval( "bar=12;in=1;foo=42;");
        System.err.println( result );
    }
    
    @Test(expected=ThrowException.class)
    public void testInvalidStatement_in2() {
        Object result = eval( "in" );
        System.err.println( result );
    }
    
}