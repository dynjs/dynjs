package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;


public class BuiltinFunctionTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testConstructor() {
        Object result = eval( "var f = new Function('x', 'return x*2;');",
                "f(42);");
        
        assertThat( result ).isEqualTo( 84 );
    }
    
    @Test
    public void testFunction() {
        Object result = eval( "var f = Function('x', 'return x*2;');",
                "f(42);");
        
        assertThat( result ).isEqualTo( 84 );
    }


}
