package org.dynjs.runtime.java;

import static org.fest.assertions.Assertions.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.junit.Test;

public class JavaIntegrationTest extends AbstractDynJSTestSupport {

    @Test
    public void testImplementAnInterfaceWithAccessToContext() {
        Object result = eval(
                "var x = 1;",
                "new java.lang.Runnable( {",
                "  run: function() { ",
                "    x = 42;",
                "  }",
                "} )");

        assertThat(result).isInstanceOf(Runnable.class);

        Object x = eval("x");
        assertThat(x).isEqualTo(1L);

        Runnable runnable = (Runnable) result;
        runnable.run();
        
        x = eval("x");
        assertThat(x).isEqualTo(42L);
    }
    
    @Test
    public void testImplementAnInterface() {
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.java.Foo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");
        
        assertThat( result.doIt() ).isEqualTo( "done in javascript" );
        
    }
    
    @Test
    public void testImplementAnAbstractClass() {
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.java.AbstractFoo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");
        
        assertThat( result.doIt() ).isEqualTo( "done in javascript" );
    }
    
    @Test
    public void testImplementAConcreteClass() {
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.java.DefaultFoo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");
        
        assertThat( result.doIt() ).isEqualTo( "done in javascript" );
    }
    
    @Test
    public void testImplementAConcreteClassWithOnlyPartialOverride() {
        
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.java.DefaultFoo( {",
                "  getContent: function() {",
                "    return 'javascript content';",
                "  }",
                "} )");
        
        assertThat( result.doItDifferently() ).isEqualTo( "done with: javascript content" );
    }
    
    @Test
    public void testImplementAConcreteClassWithOnlyPartialOverrideInversely() {
        
        Foo result = (Foo) eval( 
                "new org.dynjs.runtime.java.DefaultFoo( {",
                "  doItDifferently: function() {",
                "    print('--------------');",
                "    print(this);",
                "    print(this.getContent);",
                "    return 'with javascript content: ' + this.getContent();",
                "  }",
                "} )");
        
        assertThat( result.doItDifferently() ).isEqualTo( "with javascript content: default content" );
    }
    
    @Test
    public void testArgumentCoercion() {
        InetSocketAddress addr = (InetSocketAddress) eval( "new java.net.InetSocketAddress(8080)" );
        assertThat( addr.getPort() ).isEqualTo( 8080 );
    }
    
    @Test
    public void testConstructorFromNettyExample() {
        
        Executor e1 = (Executor) eval( "java.util.concurrent.Executors.newCachedThreadPool()");
        
        assertThat( e1 ).isNotNull();
        
        NettyFactory factory = (NettyFactory) eval( 
                "var Executors = java.util.concurrent.Executors;",
                "new org.dynjs.runtime.java.NettyFactory( Executors.newCachedThreadPool(), Executors.newCachedThreadPool() );");
        
        assertThat( factory ).isNotNull();
        
        assertThat( factory.getE1() ).isNotNull();
        assertThat( factory.getE2() ).isNotNull();
        
    }
    
}
