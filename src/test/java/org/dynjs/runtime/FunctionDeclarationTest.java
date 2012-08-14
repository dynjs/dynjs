package org.dynjs.runtime;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;


public class FunctionDeclarationTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testFunctionDeclaration() {
        eval( "function foo() { };");
        Reference foo = getContext().resolve(  "foo"  );
        assertThat( foo ).isNotNull();
        Object fn = foo.getValue( getContext() );
        assertThat( fn ).isNotNull();
        
    }

}
