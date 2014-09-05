package org.dynjs.runtime.java;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractDynJSTestSupport;
import org.dynjs.runtime.Types;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class JavaIntegrationTest extends AbstractDynJSTestSupport {

    @Test
    public void testJavaMapDeleteWithSquareBrackets() {
        eval( "var m = new java.util.HashMap()");
        eval("m.foo = 42");
        assertThat( eval( "m['foo']" ) ).isEqualTo(42L);
        eval("delete m['foo']");
        assertThat( eval( "m['foo']" ) ).isEqualTo( Types.UNDEFINED );
    }

    @Test
    public void testJavaMapDeleteWithDots() {
        eval( "var m = new java.util.HashMap()");
        eval( "m.foo = 42" );
        assertThat( eval( "m.foo" ) ).isEqualTo(42L);
        eval( "delete m.foo" );
        assertThat( eval( "m.foo" ) ).isEqualTo( Types.UNDEFINED );
    }
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
    public void testInterfaceStaticFinalFields() {
        String result = (String) eval("org.dynjs.runtime.java.Foo.SOME_STATIC_FINAL_STRING");
        assertThat(result).isEqualTo("a static final string on an interface");
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
    public void testDYNJS_108() {
        eval("javaThing = new org.dynjs.runtime.java.Thing()");
        eval("jsThing = { foo: 'bar' }");
        assertThat(eval("javaThing.dynObjectMethod(jsThing)")).isEqualTo("bar");
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
    public void testJavaArrayCoercionInMethodCalls() {
        eval("thing = new org.dynjs.runtime.java.Thing()");
        assertThat(eval("thing.stringJoiner(['foo', 'bar'])")).isEqualTo("foobar");
        // now make sure primitives work
        assertThat(eval("thing.booleanJoiner([true, false])")).isEqualTo("truefalse");
        assertThat(eval("thing.byteJoiner([1, 2, 3])")).isEqualTo("123");
        assertThat(eval("thing.charJoiner(['a', 'b', 'c'])")).isEqualTo("abc");
        assertThat(eval("thing.doubleJoiner([1.2, 2.3, 3.4])")).isEqualTo("1.22.33.4");
        assertThat(eval("thing.floatJoiner([1.2, 2.3, 3.4])")).isEqualTo("1.22.33.4");
        assertThat(eval("thing.intJoiner([1, 2, 3])")).isEqualTo("123");
        assertThat(eval("thing.longJoiner([1, 2, 3])")).isEqualTo("123");
        assertThat(eval("thing.shortJoiner([1, 2, 3])")).isEqualTo("123");
    }

    @Test
    public void testJavaArrayCoercionInCtor() {
        eval("new org.dynjs.runtime.java.Thing()");
        assertThat(eval("new org.dynjs.runtime.java.Thing([new java.util.ArrayList()])")).isInstanceOf(Thing.class);
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

        assertThat(eval("map.foo")).isEqualTo("tacos");

    }

    @Test
    public void testGenericMapFactory() throws InterruptedException {
        try {
            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            eval("var thingy = org.dynjs.runtime.java.GenericMapFactory.create()");
            eval("thingy.put(1, 'one')");
            assertThat(eval("thingy.get(1)")).isEqualTo("one");
        } finally {
            System.err.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
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
        Object result = eval("var foo = new org.dynjs.runtime.java.DefaultFoo();",
                "var js = {};",
                "js.taco = foo.getContent.bind(foo);",
                "js.taco()");

        assertThat(result).isEqualTo("default content");
        System.err.println(result);
    }

    @Test
    public void testJavaMethodWithApply() {
        Object result = eval(
                "var foo = new org.dynjs.runtime.java.DefaultFoo();",
                "var args = [ 'tacos', 42, 'cheese' ];",
                "foo.doIt.apply( foo, args );"
        );

        assertThat( result ).isEqualTo( "Another way");

        result = eval(
                "args = [ 'tacos', 42];",
                "foo.doIt.apply( foo, args );"
        );

        assertThat( result ).isEqualTo( "One way" );
    }

    @Test
    public void testJavaMethodWithSAMAndApply() {
        Object result = eval(
                "var foo = new org.dynjs.runtime.java.GenericDispatcher();",
                "var handler = foo.handle;",
                "var args = [ function() { return 'natch!' }];",
                "handler.apply( foo, args );"
        );

        assertThat( result ).isEqualTo( "natch!");
    }

    @Test
    public void testLongPrimitiveCoercion() {
        Object result = eval("new org.dynjs.runtime.java.Thing().longMethod(42)");

        assertThat(result).isInstanceOf(Long.class).isEqualTo(42L);
    }

    @Test
    public void testCheckingPropertiesOnJavaObjects() {
        eval("var thing = new org.dynjs.runtime.java.Thing();");
        // Check the existence of a property - should return undefined
        Object result = eval("thing.propertyThatDoesNotExist");
        assertThat(result).isEqualTo(Types.UNDEFINED);
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
    @Ignore
    public void testSequentialInvocation() {
        Object intResult = eval(" new org.dynjs.runtime.java.Thing().intMethod(9)");
        assertThat(intResult).isInstanceOf(String.class).isEqualTo("9");

        Object longResult = eval("new org.dynjs.runtime.java.Thing().longMethod(42)");
        assertThat(longResult).isInstanceOf(Long.class).isEqualTo(42L);
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

    @Test
    public void testJavascriptMethodsOnJavaObjects() {
        eval("var f = new Packages.me.skippy.dolphin.DorsalFin( {",
                "isTrue: function() {",
                "  return true",
                "}});");
        assertThat(eval("f.isTrue()")).isEqualTo(true);
    }

    @Test
    public void testJavascriptMethodsOnJavaObjects2() {
        eval("var f = new Packages.me.skippy.dolphin.DorsalFin();",
                "f.isTrue = function() {",
                "  return true",
                "}});");
        assertThat(eval("f.isTrue()")).isEqualTo(true);
    }

    @Test
    public void testAbstractBooleanMethods() {
        eval("var b = new org.dynjs.runtime.java.AbstractBar( {",
                "isHoppy: function() {",
                "  return true",
                "}});");
        assertThat(eval("b.isHoppy()")).isEqualTo(true);
    }

    @Test
    public void testByte() {
        Object result = eval("var b = new java.lang.Byte(120);", "b");
        assertThat(result).isInstanceOf(Byte.class);
    }

    @Test
    public void testDouble() {
        Object result = eval("var d = new java.lang.Double(1.234);", "d");
        assertThat(result).isInstanceOf(Double.class);
    }

    @Test
    public void testCallProtectedMethodOfConcreteClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {} );");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callConcreteGetLong()")).isEqualTo(-1L);
        assertThat(b.callConcreteGetLong()).isEqualTo(-1L);
    }

    @Test
    public void testOverrideProtectedMethodOfConcreteClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {",
                "concreteGetLong: function() {",
                "  return 1;",
                "}});");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callConcreteGetLong()")).isEqualTo(1L);
        assertThat(b.callConcreteGetLong()).isEqualTo(1L);
    }

    @Test
    public void testCallProtectedMethodImplementedByConcreteClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {} );");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callAbstractGetLong()")).isEqualTo(-2L);
        assertThat(b.callAbstractGetLong()).isEqualTo(-2L);
    }

    @Test
    public void testOverrideProtectedMethodImplementedByConcreteClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {",
                "abstractGetLong: function() {",
                "  return 2;",
                "}});");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callAbstractGetLong()")).isEqualTo(2L);
        assertThat(b.callAbstractGetLong()).isEqualTo(2L);
    }

    @Test
    public void testCallInheritedProtectedMethodImplementedByAbstractClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {} );");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(b.callImplementedGetLong()).isEqualTo(-3L);
        assertThat(eval("b.callImplementedGetLong()")).isEqualTo(-3L);
    }

    @Test
    public void testOverrideInheritedProtectedMethodImplementedByAbstractClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {",
                "implementedGetLong: function() {",
                "  return 3;",
                "}});");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callImplementedGetLong()")).isEqualTo(3L);
        assertThat(b.callImplementedGetLong()).isEqualTo(3L);
    }

    @Test
    public void testCallProtectedMethodImplementedByAbstractClass() {
        eval("var b = new org.dynjs.runtime.java.SomeAbstractBean( {} );");
        SomeAbstractBean b = (SomeAbstractBean) eval("b");

        assertThat(eval("b.callImplementedGetLong()")).isEqualTo(-3L);
        assertThat(b.callImplementedGetLong()).isEqualTo(-3L);
    }

    @Test
    public void testOverrideProtectedMethodImplementedByAbstractClass() {
        eval("var b = new org.dynjs.runtime.java.SomeAbstractBean( {",
                "implementedGetLong: function() {",
                "  return 4;",
                "}});");
        SomeAbstractBean b = (SomeAbstractBean) eval("b");

        assertThat(eval("b.callImplementedGetLong()")).isEqualTo(4L);
        assertThat(b.callImplementedGetLong()).isEqualTo(4L);
    }

    @Test
    public void testImplementProtectedMethodOfAbstractClass() {
        eval("var b = new org.dynjs.runtime.java.SomeAbstractBean( {",
                "abstractGetLong: function() {",
                "  return 5;",
                "}});");
        SomeAbstractBean b = (SomeAbstractBean) eval("b");

        assertThat(eval("b.callAbstractGetLong()")).isEqualTo(5L);
        assertThat(b.callAbstractGetLong()).isEqualTo(5L);
    }

    @Test
    public void testCallProtectedMethodOfHigherParent() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {} );");
        SomeAbstractBean b = (SomeAbstractBean) eval("b");

        assertThat(eval("b.callImplementedParentGetLong()")).isEqualTo(-6L);
        assertThat(b.callImplementedParentGetLong()).isEqualTo(-6L);
    }

    @Test
    public void testOverrideProtectedMethodOfHigherParent() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {",
                "implementedParentGetLong: function() {",
                "  return 6;",
                "}});");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callImplementedParentGetLong()")).isEqualTo(6L);
        assertThat(b.callImplementedParentGetLong()).isEqualTo(6L);
    }

    @Test
    @Ignore
    public void testBindFunctionThatOverridesMethodOnOnSubclass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean( {} );",
                "b.implementedParentGetLong = function() {",
                "  return 7;",
                "};");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callImplementedParentGetLong()")).isEqualTo(7L);
        assertThat(b.callImplementedParentGetLong()).isEqualTo(7L);
    }

    @Test
    @Ignore
    public void testBindFunctionThatOverridesMethodOnOriginalClass() {
        eval("var b = new org.dynjs.runtime.java.SomeConcreteBean();",
                "b.implementedParentGetLong = function() {",
                "  return 8;",
                "};");
        SomeConcreteBean b = (SomeConcreteBean) eval("b");

        assertThat(eval("b.callImplementedParentGetLong()")).isEqualTo(8L);
        assertThat(b.callImplementedParentGetLong()).isEqualTo(8L);
    }

    @Test
    public void testPrimitivesInAbstractClassMethodSignature() {
        eval("var impl = { " +
                "doItWithParameters: function(param, tf) { return param + (tf ? 'true' : 'false'); }" +
                "};" +
                "var foobar = new org.dynjs.runtime.java.Foobar(impl);");

        assertThat(eval("foobar.doItWithParameters('some string', true);")).isEqualTo("some stringtrue");
    }

    @Test
    public void testNumbersInAbstractClassMethodSignature() {
        eval("var doInt = function(param) { return param + 1; };" +
                "var impl = { " +
                "doItWithInt: doInt," +
                "doItWithPrimitiveInt: doInt," +
                "doItWithLong: doInt," +
                "doItWithPrimitiveLong: doInt," +
                "doItWithShort: doInt," +
                "doItWithPrimitiveShort: doInt," +
                "doItWithFloat: function(param) { return param + 1.1; }," +
                "doItWithPrimitiveFloat: function(param) { return param + 1.1; }," +
                "doItWithDouble: function(param) { return param + 1.1; }," +
                "doItWithPrimitiveDouble: function(param) { return param + 1.1; }," +
                "};" +
                "var foobar = new org.dynjs.runtime.java.Foobar(impl);");

        assertThat(eval("foobar.doItWithInt(5);")).isEqualTo(6);
        assertThat(eval("foobar.callWithInt(5);")).isEqualTo(6);
        assertThat(eval("foobar.doItWithPrimitiveInt(5);")).isEqualTo(6);
        assertThat(eval("foobar.callWithPrimitiveInt(5);")).isEqualTo(6);

        assertThat(eval("foobar.doItWithLong(5);")).isEqualTo(6L);
        assertThat(eval("foobar.callWithLong(5);")).isEqualTo(6L);
        assertThat(eval("foobar.doItWithPrimitiveLong(5);")).isEqualTo(6L);
        assertThat(eval("foobar.callWithPrimitiveLong(5);")).isEqualTo(6L);

        assertThat(eval("foobar.doItWithShort(5);")).isEqualTo((short) 6);
        assertThat(eval("foobar.callWithShort(5);")).isEqualTo((short) 6);
        assertThat(eval("foobar.doItWithPrimitiveShort(5);")).isEqualTo((short) 6);
        assertThat(eval("foobar.callWithPrimitiveShort(5);")).isEqualTo((short) 6);

        assertThat(eval("foobar.doItWithFloat(5.0);")).isEqualTo(6.1f);
        assertThat(eval("foobar.callWithFloat(5.0);")).isEqualTo(6.1f);
        assertThat(eval("foobar.doItWithPrimitiveFloat(5.0);")).isEqualTo(6.1f);
        assertThat(eval("foobar.callWithPrimitiveFloat(5.0);")).isEqualTo(6.1f);

        assertThat(eval("foobar.doItWithDouble(5.0);")).isEqualTo(6.1d);
        assertThat(eval("foobar.callWithDouble(5.0);")).isEqualTo(6.1d);
        assertThat(eval("foobar.doItWithPrimitiveDouble(5.0);")).isEqualTo(6.1d);
        assertThat(eval("foobar.callWithPrimitiveDouble(5.0);")).isEqualTo(6.1d);
    }

    @Test
    public void testBooleansInAbstractClassMethodSignature() {
        eval("var impl = { " +
                "doItWithBoolean: function(param) { return param; }," +
                "doItWithPrimitiveBoolean: function(param) { return true; }" +
                "};" +
                "var foobar = new org.dynjs.runtime.java.Foobar(impl);");

        assertThat(eval("foobar.doItWithBoolean(true);")).isEqualTo(true);
        assertThat(eval("foobar.callWithBoolean(true);")).isEqualTo(true);
        assertThat(eval("foobar.doItWithPrimitiveBoolean(true);")).isEqualTo(true);
        assertThat(eval("foobar.callWithPrimitiveBoolean(true);")).isEqualTo(true);
    }

    @Test
    public void testCharactersInAbstractClassMethodSignature() {
        eval("var impl = { " +
                "doItWithChar: function(param) { return 'c'; }," +
                "doItWithPrimitiveChar: function(param) { return 'c'; }" +
                "};" +
                "var foobar = new org.dynjs.runtime.java.Foobar(impl);");

        assertThat(eval("foobar.doItWithChar('a');")).isEqualTo('c');
        assertThat(eval("foobar.callWithChar('a');")).isEqualTo('c');
        assertThat(eval("foobar.doItWithPrimitiveChar('a');")).isEqualTo('c');
        assertThat(eval("foobar.callWithPrimitiveChar('a');")).isEqualTo('c');
    }

    @Test
    public void testBytesInAbstractClassMethodSignature() {
        eval("var impl = { " +
                "doItWithByte: function(param) { return 1; }," +
                "doItWithPrimitiveByte: function(param) { return 1; }" +
                "};" +
                "var foobar = new org.dynjs.runtime.java.Foobar(impl);");

        assertThat(eval("foobar.doItWithByte(0);")).isEqualTo((byte) 1);
        assertThat(eval("foobar.callWithByte(0);")).isEqualTo((byte) 1);
        assertThat(eval("foobar.doItWithPrimitiveByte(0);")).isEqualTo((byte) 1);
        assertThat(eval("foobar.callWithPrimitiveByte(0);")).isEqualTo((byte) 1);
    }

    @Test
    @Ignore // currently we can't override any java methods that return arrays
    public void testArraysInAbstractClassMethodSignature() {
        eval("var impl = { " +
                "doItWithPrimitiveIntArray: function(param) { return [1, 2, 3]; }," +
                "};" +
                "var foobar = new org.dynjs.runtime.java.Foobar(impl);");

        assertThat(eval("foobar.doItWithPrimitiveIntArray([0]);")).isEqualTo((byte) 1);
    }

    @Test
    public void testSomeInterface() {
        assertThat(eval("var obj = new org.dynjs.runtime.java.SomeInterface({" +
                "handleObjectBoolean: function(o) { return true; }," +
                "handleObjectInt: function(o) { return 5; }," +
                "handleObjectLong: function(o) { return 5; }," +
                "handleObjectShort: function(o) { return 5; }," +
                "handleObjectFloat: function(o) { return 1.0; }," +
                "handleObjectDouble: function(o) { return 1.1; }," +
                "handleObjectChar: function(o) { return 'c'; }," +
                "handleObjectByte: function(o) { return 1; }," +
                "handleObjectString: function(o) { return 'bar'; }" +
                "});"));
        assertThat(eval("obj.handleObjectBoolean('foo')")).isEqualTo(true);
        assertThat(eval("obj.handleObjectInt('foo')")).isEqualTo(5);
        assertThat(eval("obj.handleObjectLong('foo')")).isEqualTo(5L);
        assertThat(eval("obj.handleObjectShort('foo')")).isEqualTo((short) 5);
        assertThat(eval("obj.handleObjectFloat('foo')")).isEqualTo(1.0f);
        assertThat(eval("obj.handleObjectDouble('foo')")).isEqualTo(1.1d);
        assertThat(eval("obj.handleObjectChar('foo')")).isEqualTo('c');
        assertThat(eval("obj.handleObjectByte('foo')")).isEqualTo((byte) 1);
        assertThat(eval("obj.handleObjectString('foo')")).isEqualTo("bar");
    }

    @Test
    public void testInstanceOfWithRHSJavaInstance() {
        eval("var thing = new org.dynjs.runtime.java.DefaultFoo()");
        assertThat(eval("thing instanceof org.dynjs.runtime.java.DefaultFoo")).isEqualTo(true);
        assertThat(eval("thing instanceof org.dynjs.runtime.java.AbstractFoo")).isEqualTo(true);
        assertThat(eval("thing instanceof org.dynjs.runtime.java.Foo")).isEqualTo(true);
        assertThat(eval("thing instanceof org.dynjs.runtime.java.Foobar")).isEqualTo(false);
    }

}
