package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

public class FunctionDeclarationTest extends AbstractDynJSTestSupport {

    @Test
    public void testFunctionDeclaration() {
        eval( "function foo() { 42 };" );
        Reference foo = getContext().resolve( "foo" );
        assertThat( foo ).isNotNull();
        assertThat( foo.isUnresolvableReference() ).isFalse();
        JSFunction fn = (JSFunction) foo.getValue( getContext() );
        assertThat( fn ).isNotNull();
        
        Object result = fn.call(getContext() );
        assertThat( result ).isEqualTo( 42 );
    }

}
