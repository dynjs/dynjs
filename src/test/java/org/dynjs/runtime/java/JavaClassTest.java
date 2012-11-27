package org.dynjs.runtime.java;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.java.UnboundJavaMethod;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assume.*;

public class JavaClassTest extends AbstractDynJSTestSupport {
    
    @Before
    public void beforeMethod() {
        assumeTrue( getConfig().isInvokeDynamicEnabled() );
    }

    @Test
    public void testJavaPackage() {
        Object result = eval("org.dynjs.runtime.java");
        assertThat(result).isInstanceOf(JavaPackage.class);
    }

    @Test
    public void testJavaClass() {
        Object result = eval("org.dynjs.runtime.java.JavaMockery");
        assertThat(result).isInstanceOf(Class.class).isSameAs(JavaMockery.class);
    }

    @Test
    public void testJavaClass_staticMethod_call() {
        Object result = eval("var cls = org.dynjs.runtime.java.JavaMockery",
                "cls.name");
        assertThat(result).isInstanceOf(String.class);
        System.err.println("name: " + result);
    }

    @Test
    public void testShadowObjects() {
        Object result = eval("var cls = org.dynjs.runtime.java.JavaMockery;",
                "cls.taco = 42",
                "org.dynjs.runtime.java.JavaMockery.taco;");

        assertThat(result).isEqualTo(42L);
    }
    
    @Test
    public void testShadowObjects_withFunction() {
        Object result = eval("var cls = org.dynjs.runtime.java.JavaMockery;",
                "var o = new cls();",
                "o.fancyPants = function() { return this.class };",
                "o.fancyPants();" );

        assertThat(result).isEqualTo(JavaMockery.class);
    }
    
    @Test
    public void testNew_java() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery");
        assertThat( result ).isInstanceOf(JavaMockery.class);
    }
    
    @Test
    public void testNew_java_withParams() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42)");
        assertThat( result ).isInstanceOf(JavaMockery.class);
        assertThat( ((JavaMockery)result).getValue() ).isEqualTo( 42L );
    }
    
    @Test
    public void testNew_java_withParams_getProp() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).value");
        assertThat( result ).isEqualTo( 42L );
    }
    
    @Test
    public void testNew_java_withParams_setProp_java() {
        Object result = eval( "o = new org.dynjs.runtime.java.JavaMockery(42)",
                "o.value = 'taco';",
                "o.value" );
        assertThat( result ).isEqualTo( "taco" );
    }
    
    @Test
    public void testNew_java_withParams_setProp_js() {
        Object result = eval( "o = new org.dynjs.runtime.java.JavaMockery( Object.prototype )",
                "o.value" );
        
        assertThat( result ).isInstanceOf(JSObject.class);
    }
    
    @Test
    public void testNew_java_withParams_getMethod() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).doSomething");
        assertThat( result ).isInstanceOf(UnboundJavaMethod.class);
        assertThat( ((UnboundJavaMethod)result).getName() ).isEqualTo( "doSomething" );
    }
    
    @Test
    public void testNew_java_withParams_getMethod_call() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).doSomething()");
        assertThat( result ).isEqualTo( 42L );
    }
    
    @Test
    public void testVoidMethod() {
        Object result = eval( "new org.dynjs.runtime.java.JavaMockery(42).somethingVoid()" );
        assertThat( result ).isEqualTo( Types.UNDEFINED );
    }
    
    @Test
    public void testCoercion() {
        Object result = eval( 
                "var jsObj = {",
                "  toJava: function() {" +
                "    return 'taco';",
                "  },",
                "};",
                "new org.dynjs.runtime.java.JavaMockery(jsObj).value;" );
        
        System.err.println( result );
        System.err.println( result.getClass() );
    }

}
