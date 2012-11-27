package org.dynjs.runtime.java;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;
import static org.fest.assertions.Assertions.*;


public class JavaClassTest extends AbstractDynJSTestSupport {
    
    
    @Test
    public void testJavaPackage() {
        Object result = eval( "org.dynjs.runtime.java" );
        assertThat( result ).isInstanceOf( JavaPackage.class);
    }
    
    @Test
    public void testJavaClass() {
        Object result = eval( "org.dynjs.runtime.java.JavaMockery" );
        assertThat( result ).isInstanceOf( Class.class).isSameAs(JavaMockery.class);
    }
    

}
