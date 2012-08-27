package org.dynjs.runtime.builtins.types;

import static org.fest.assertions.Assertions.*;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSFunction;
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
    
    @Test
    public void testFunctionWithGlobbedUpFormalParams() {
        Object result = eval( "var f = Function('x,y', 'return x*y*2;');",
                "f(42,10);");
        
        assertThat( result ).isEqualTo( 840 );
    }
    
    @Test
    public void testFunctionLength() {
        assertThat( eval( "Function.length" ) ).isEqualTo(1);
    }
    
    @Test
    public void testPrototypeConstructor() {
        assertThat( eval( "Function.prototype.constructor" ) ).isNotNull().isInstanceOf(JSFunction.class);
        assertThat( eval( "Function.prototype.constructor" ) ).isSameAs( eval( "Function" ) );
    }
    
    @Test
    public void testToString() {
        Object str = eval( "Function('x', 'if(false){}else{};return x*2;').toString()" );
    }
    
    @Test
    public void testToStringNative() {
        Object str = eval( "parseInt.toString()" );
        System.err.println( str );
    }


}
