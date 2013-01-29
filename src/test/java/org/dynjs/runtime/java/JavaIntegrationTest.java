package org.dynjs.runtime.java;

import static org.fest.assertions.Assertions.*;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.junit.Ignore;
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
    
    @Test
    @Ignore
    public void testRawJavaArrays() {
        eval("javaBytes = new java.lang.Byte[3]");
        assertThat(eval("javaBytes.length")).isEqualTo(3);
    }
    
    @Test
    public void testReturnedJavaArrays() {
        eval("javaThing = new org.dynjs.runtime.java.JavaMockery()");
        eval("javaArray = javaThing.getAFewThings()");
        assertThat(eval("javaArray.length")).isEqualTo(3);
        assertThat(eval("javaArray[2]")).isEqualTo( "FooBar" );
    }
    
    @Test
    public void testArrayElementSetting() {
        eval("javaThing = new org.dynjs.runtime.java.JavaMockery()");
        eval("javaArray = javaThing.getAFewThings()");
        assertThat(eval("javaArray.length")).isEqualTo(3);
        eval( "javaArray[2] = 'bob'");
        assertThat(eval("javaArray[2]")).isEqualTo( "bob" );
    }
    
    @Test
    public void testNettyChannelHandler() {
        Object result = eval( "new org.jboss.netty.channel.SimpleChannelHandler({",
                "})");
        
        assertThat( result ).isInstanceOf(SimpleChannelHandler.class);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testGeneric() {
        GenericHandler<Thing> handler = (GenericHandler<Thing>) eval( 
                "handler = new org.dynjs.runtime.java.GenericHandler({",
                "  handle: function(thing) {",
                "    return 'handled a thing';",
                "  }",
                "});" );
        
        Dispatcher dispatcher = new Dispatcher();
        Object result = dispatcher.handle(handler);
        assertThat( result ).isEqualTo( "handled a thing" );
        
    }
    
    @Test
    public void testGenericParam() {
        Object result = eval( 
                "handler = new org.dynjs.runtime.java.GenericHandler({",
                "  handle: function(thing) {",
                "    return 'handled a thing';",
                "  }",
                "});",
                "dispatcher = new org.dynjs.runtime.java.Dispatcher();",
                "dispatcher.handle(handler)" );
        
        assertThat( result ).isEqualTo( "handled a thing" );
        
    }
    
    @Test
    public void testGenericParamDoubly() {
        Object result = eval( 
                "handler = new org.dynjs.runtime.java.GenericHandler({",
                "  handle: function(thing) {",
                "    return 'handled a thing';",
                "  }",
                "});",
                "dispatcher = new org.dynjs.runtime.java.GenericDispatcher();",
                "dispatcher.handle(handler)" );
        
        assertThat( result ).isEqualTo( "handled a thing" );
        
    }
    
    @Test
    public void testSAM() {
        Object result = eval( 
                "catcher = new org.dynjs.runtime.java.HandlerCatcher();",
                "catcher.catchHandler( function() { } );"
                );
        
        assertThat( result ).isInstanceOf( GenericHandler.class );
    }
}
