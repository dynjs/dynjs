package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;

import org.dynjs.Config;
import org.dynjs.runtime.java.JavaMockery;
import org.junit.Ignore;
import org.junit.Test;

public class InvokeDynamicTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testObjectProperties() {
        Object result = eval( 
                "var o = {",
                "  cheese: 'swiss'",
                "};",
                "o.cheese" );
        
        assertThat( result ).isEqualTo( "swiss" );
    }
    
    @Test
    public void testEnvironment() {
        Object result = eval( 
                "var o = 'swiss cheese';",
                "o" );
        
        assertThat( result ).isEqualTo( "swiss cheese" );
    }
    
    @Test
    public void testFunctionExpression() {
        Object result = eval( "(function(){ return 'taco'; })()" );
        assertThat( result ).isEqualTo("taco");
    }
    
    @Test
    public void testEnvironmentFunction() {
        Object result = eval( "function foo(){ return 'taco'; };",
                "foo()");
        assertThat( result ).isEqualTo("taco");
    }
    
    @Test
    public void testEnvironmentFunctionInVar() {
        Object result = eval( 
                "var o = function(){ return 'swiss cheese'; }",
                "o()" );
        
        assertThat( result ).isEqualTo( "swiss cheese" );
    }
    
    @Test
    public void testObjectFunction() {
        Object result = eval( 
                "var o = {",
                "  cheese: function(){ return 'swiss'; }",
                "};",
                "o.cheese()" );
        
        assertThat( result ).isEqualTo( "swiss" );
    }
    

    @Test
    public void testNew_java() {
        if (this.config.getCompileMode() == Config.CompileMode.FORCE ) {
            Object result = eval( "new org.dynjs.runtime.java.JavaMockery");
            assertThat( result ).isInstanceOf(JavaMockery.class);
        }
    }
    
    @Test
    public void testNew_java_withParams() {
        if (this.config.getCompileMode() == Config.CompileMode.FORCE ) {
            Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42)");
            assertThat( result ).isInstanceOf(JavaMockery.class);
            assertThat( ((JavaMockery)result).getValue() ).isEqualTo( 42L );
        }
    }
    
    /*
    @Test
    public void testNew_java_withParams_getProp() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).value");
        assertThat( result ).isEqualTo( 42L );
    }
    
    @Test
    public void testNew_java_withParams_getMethod() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).doSomething");
        assertThat( result ).isInstanceOf(MethodMeta.class);
        assertThat( ((MethodMeta)result).getName() ).isEqualTo( "doSomething" );
    }
    
    @Test
    public void testNew_java_withParams_getMethod_call() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).doSomething()");
        assertThat( result ).isEqualTo( 42L );
    }

*/
}
