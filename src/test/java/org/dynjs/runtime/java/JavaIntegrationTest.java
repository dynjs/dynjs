package org.dynjs.runtime.java;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.junit.Ignore;
import org.junit.Test;

public class JavaIntegrationTest extends AbstractDynJSTestSupport {
    
    @Test
    public void testJavaStringEquality() {
        eval("var f1 = new java.io.File('/tmp/foo')");
        eval("var f2 = new java.io.File('/tmp/foo')");
        eval("var f3 = f1");
        assertThat(eval("f1.equals(f2)")).isEqualTo(true);
        File f1 = (File) eval("f1");
        File f2 = (File) eval("f2");
        File f3 = (File) eval("f3");
        assertThat(f1.equals(f2)).isEqualTo(true);
        assertThat(f1.equals(f3)).isEqualTo(true);
        assertThat(eval("f1 === f2")).isEqualTo(false);
        assertThat(eval("f1 == f2")).isEqualTo(false);
        assertThat(eval("f1 == f3")).isEqualTo(true);
        assertThat(eval("f1 === f3")).isEqualTo(true);
    }
    
    @Test
    public void testCallJavaMethodWithPrimitiveBooleanParameter() {
        eval("var thing = new org.dynjs.runtime.java.Thing()");
        assertThat(eval("thing.oppositeOf(true)")).isEqualTo(false);
    }

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

        assertThat(result.doIt()).isEqualTo("done in javascript");

    }

    @Test
    public void testImplementAnAbstractClass() {
        Foo result = (Foo) eval(
                "new org.dynjs.runtime.java.AbstractFoo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");

        assertThat(result.doIt()).isEqualTo("done in javascript");
    }

    @Test
    public void testImplementAConcreteClass() {
        Foo result = (Foo) eval(
                "new org.dynjs.runtime.java.DefaultFoo( {",
                "  doIt: function() {",
                "    return 'done in javascript';",
                "  }",
                "} )");

        assertThat(result.doIt()).isEqualTo("done in javascript");
    }

    @Test
    public void testImplementAConcreteClassWithOnlyPartialOverride() {

        Foo result = (Foo) eval(
                "new org.dynjs.runtime.java.DefaultFoo( {",
                "  getContent: function() {",
                "    return 'javascript content';",
                "  }",
                "} )");

        assertThat(result.doItDifferently()).isEqualTo("done with: javascript content");
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

        assertThat(result.doItDifferently()).isEqualTo("with javascript content: default content");
    }

    @Test
    public void testArgumentCoercion() {
        InetSocketAddress addr = (InetSocketAddress) eval("new java.net.InetSocketAddress(8080)");
        assertThat(addr.getPort()).isEqualTo(8080);
    }

    @Test
    public void testConstructorFromNettyExample() {

        Executor e1 = (Executor) eval("java.util.concurrent.Executors.newCachedThreadPool()");

        assertThat(e1).isNotNull();

        NettyFactory factory = (NettyFactory) eval(
                "var Executors = java.util.concurrent.Executors;",
                "new org.dynjs.runtime.java.NettyFactory( Executors.newCachedThreadPool(), Executors.newCachedThreadPool() );");

        assertThat(factory).isNotNull();

        assertThat(factory.getE1()).isNotNull();
        assertThat(factory.getE2()).isNotNull();

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
        assertThat(eval("javaArray[2]")).isEqualTo("FooBar");
    }

    @Test
    public void testArrayElementSetting() {
        eval("javaThing = new org.dynjs.runtime.java.JavaMockery()");
        eval("javaArray = javaThing.getAFewThings()");
        assertThat(eval("javaArray.length")).isEqualTo(3);
        eval("javaArray[2] = 'bob'");
        assertThat(eval("javaArray[2]")).isEqualTo("bob");
    }

    @Test
    public void testNettyChannelHandler() {
        Object result = eval("new org.jboss.netty.channel.SimpleChannelHandler({",
                "})");

        assertThat(result).isInstanceOf(SimpleChannelHandler.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGeneric() {
        GenericHandler<Thing> handler = (GenericHandler<Thing>) eval(
                "handler = new org.dynjs.runtime.java.GenericHandler({",
                "  handle: function(thing) {",
                "    return 'handled a thing';",
                "  }",
                "});");

        Dispatcher dispatcher = new Dispatcher();
        Object result = dispatcher.handle(handler);
        assertThat(result).isEqualTo("handled a thing");

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
                "dispatcher.handle(handler)");

        assertThat(result).isEqualTo("handled a thing");

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
                "dispatcher.handle(handler)");

        assertThat(result).isEqualTo("handled a thing");

    }

    @Test
    public void testSAM() {
        Object result = eval(
                "catcher = new org.dynjs.runtime.java.HandlerCatcher();",
                "catcher.catchHandler( function() { } );"
                );

        assertThat(result).isInstanceOf(GenericHandler.class);
    }

    @Test
    public void testShadowObjects() {
        eval(
                "var thing = new org.dynjs.runtime.java.JavaMockery();",
                "var other_thing = new org.dynjs.runtime.java.JavaMockery();",
                "thing.tacos = 'tasty';",
                "other_thing.tacos = 'ugly';");

        assertThat(eval("thing.tacos")).isEqualTo("tasty");
        assertThat(eval("other_thing.tacos")).isEqualTo("ugly");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMapLikeness() {

        Map<String, Object> map = (Map<String, Object>) eval("x = {",
                "foo: 'taco',",
                "bar: 42,",
                "};");

        assertThat(map.get("foo")).isEqualTo("taco");
        assertThat(map.get("bar")).isEqualTo(42L);

        assertThat(map.keySet().size()).isEqualTo(2);
        assertThat(map.keySet()).contains("foo");
        assertThat(map.keySet()).contains("bar");

        map.keySet().remove("foo");

        assertThat(map.get("foo")).isNull();

        assertThat(eval("x.foo")).isEqualTo(Types.UNDEFINED);
    }

    @Test
    public void testMapParameter() {
        Object result = eval(
                "var consumer = new org.dynjs.runtime.java.MapConsumer();",
                "var obj = { foo: 'tacos', fish: 'haddock' };",
                "consumer.consume( obj, 'fish');"
                );

        assertThat(result).isEqualTo("haddock");
    }

    @Test
    public void testJavaMapsAsJSObjects() {
        Map map = (Map) eval(
                "var map = new java.util.HashMap();",
                "map.foo = 'tacos';",
                "map"
                );

        assertThat(map.get("foo")).isEqualTo("tacos");
        
        assertThat( eval( "map.foo" ) ).isEqualTo( "tacos" );

    }
    
    @Test    
    public void testGenericMapFactory() {
        eval("var thingy = org.dynjs.runtime.java.GenericMapFactory.create()");
        eval("thingy.put(1, 'one')");
        assertThat(eval("thingy.get(1)")).isEqualTo("one");
    }
    
    @Test
    public void testGenericFactory() {
        eval("var nonmap = org.dynjs.runtime.java.GenericMapFactory.createNonMap();");
        assertThat(eval("nonmap.sayHello('hello generic')")).isEqualTo("hello generic");
    }
    
    @Test
    public void testMethodOverloading() {
        eval("var thing = new org.dynjs.runtime.java.DefaultFoo();");
        //assertThat(eval("thing.doIt('foo', 12)")).isEqualTo("One way");
        //assertThat(eval("thing.doIt('foo', 12, 'bar')")).isEqualTo("Another way");
        //assertThat(eval("thing.doIt('foo', 12, 'bar', 22)")).isEqualTo("Yet another way");
        assertThat(eval("thing.doIt('foo', null, 'bar', null)")).isEqualTo("Yet another way");
    }
    
    @Test
    public void testBoundJavaMethod() {
        Object result = eval( "var foo = new org.dynjs.runtime.java.DefaultFoo();",
                "var js = {};",
                "js.taco = foo.getContent.bind(foo);",
                "js.taco()" );
        
        assertThat( result ).isEqualTo( "default content" );
        System.err.println( result );
    
        
    }
    
    @Test
    public void testLongPrimitiveCoercion() {
        Object result = eval( "new org.dynjs.runtime.java.Thing().longMethod(42)");
        
        assertThat( result ).isInstanceOf(Long.class).isEqualTo(42L);
    }
    
    @Test
    public void testCheckingPropertiesOnJavaObjects() {
        eval("var thing = new org.dynjs.runtime.java.Thing();");
        // Check the existence of a property - should return undefined
        Object result = eval("thing.propertyThatDoesNotExist");
        assertThat( result ).isEqualTo( Types.UNDEFINED );
    }
    
    @Test
    public void testPublicNumericAttributes() {
        eval("var thing = new org.dynjs.runtime.java.Thing();");
        assertThat(eval("thing.someNum")).isEqualTo(100);
        eval("thing.someNum = 200");
        assertThat(eval("thing.someNum")).isEqualTo(200);
    }
    
    @Test
    public void testIntegerCoercion() {
        eval("var thing = new org.dynjs.runtime.java.Thing();");
        assertThat(eval("thing.intMethod(9)")).isEqualTo("9");
    }
    
    @Test
    public void testIoPackage() {
        assertThat(eval("io")).isInstanceOf(JavaPackage.class);
    }
    
    @Test
    public void testPackages() {
        assertThat(eval("Packages")).isInstanceOf(JavaPackage.class);
        assertThat(eval("Packages.io")).isInstanceOf(JavaPackage.class);
        assertThat(eval("Packages.me.skippy.dolphin")).isInstanceOf(JavaPackage.class);
        assertThat(eval("new Packages.me.skippy.dolphin.DorsalFin()")).isInstanceOf(me.skippy.dolphin.DorsalFin.class);
        try {
            assertThat(eval("me.skippy.dolphin")).isInstanceOf(JavaPackage.class);
            fail("Expected a reference error");
        } catch (ThrowException e) {
            // expected
        }
    }
    
    @Test
    public void testBindJavascriptFunctionToJavaObject() {
        eval("var f = function() { return 'cheetoes' }");
        eval("var o = new Packages.me.skippy.dolphin.DorsalFin();");
        eval("o.snack = f;");
        assertThat(eval("o.snack();")).isEqualTo("cheetoes");
    }
}
